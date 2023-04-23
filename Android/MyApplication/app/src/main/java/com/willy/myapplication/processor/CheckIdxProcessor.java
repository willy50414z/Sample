package com.willy.myapplication.processor;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.willy.myapplication.constant.Const;
import com.willy.myapplication.enumeration.TableEnum;
import com.willy.myapplication.parser.TrackListParser;
import com.willy.myapplication.parser.TrackTargetParser;
import com.willy.myapplication.po.TrackListPO;
import com.willy.myapplication.po.TrackTargetPO;
import com.willy.myapplication.util.DBUtil;
import com.willy.myapplication.util.DateUtil;
import com.willy.myapplication.util.NotificationUtil;
import com.willy.myapplication.util.TypeUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckIdxProcessor {
    private Context ctx;

    public CheckIdxProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public void check() {
        NotificationUtil notUtil = null;
        Map<String, String> paramMap = new HashMap<>();
        DBUtil db = new DBUtil(ctx);
        List<TrackTargetPO> ttList = db.query("select * from " + TableEnum.TRACK_TARGET, new TrackTargetParser());
        boolean hasAnyNotify = false;
        //cleanDataFileDir();
        for (TrackTargetPO tt : ttList) {
            try {
                FetchDataProcessor dataProc = (FetchDataProcessor) Class.forName(Const._PKG_NAME_PROCESSOR + ".Fetch" + tt.getTargetProc() + "Processor").newInstance();
                if (tt.getTargetProcArgs() != null && tt.getTargetProcArgs().length() > 0) {
                    dataProc.setSidCode(tt.getTargetProcArgs());
                }
                double oldIndex = tt.getLastCheckIndex();
                double newIndex = dataProc.getLastestIndex();
                List<TrackListPO> tlList = db.query("select * from " + TableEnum.TRACK_LIST
                                + " where ((UP_LIMIT > ?1 and DN_LIMIT <= ?1) or (UP_LIMIT > ?2 and DN_LIMIT <= ?2)) and TRACK_TARGET = ?3", new TrackListParser()
                        , String.valueOf(oldIndex), String.valueOf(newIndex), String.valueOf(tt.getId()));

                //debug toast
//                Toast.makeText(ctx, oldIndex + "/" + newIndex + "/" + tt.getId() + "/" + tlList.size(), Toast.LENGTH_LONG).show();

                if (tlList.size() > 1) {
                    TrackListPO oldTl = tlList.stream().filter(tl -> (tl.getUpLimit().doubleValue() > oldIndex && tl.getDnLimit().doubleValue() <= oldIndex)).findFirst().orElse(null);
                    TrackListPO newTl = tlList.stream().filter(tl -> (tl.getUpLimit().doubleValue() > newIndex && tl.getDnLimit().doubleValue() <= newIndex)).findFirst().orElse(null);
                    String logMsg = tt.getTargetName() + "\r\n上次檢測時間: "
                            + TypeUtil.dateToStr(tt.getLastCheckDate(), DateUtil.dateFormat_DateTime_Dash) + "\r\n上次檢測時指數: "
                            + tt.getLastCheckIndex() + "\r\n上次投資金額: " + oldTl.getAmt() + "\r\n\r\n本次檢測指數: " + newIndex
                            + "\r\n本次投資金額: " + newTl.getAmt();
                    if (notUtil == null) {
                        notUtil = new NotificationUtil(ctx, "CheckIdxProcessor-1", "CheckIdxProcessorChannel");
                        notUtil.clearAllNotification();
                    }
                    paramMap = new HashMap<>();
                    paramMap.put("indexCheckLog", logMsg);
                    notUtil.addNotification("投資金額異動通知", "標的: " + tt.getTargetName() + " 原始投資金額: " + oldTl.getAmt() + " 應更新投資金額: " + newTl.getAmt() + "\n點擊查看詳細資訊"
                            , Const._ACTION_INDEX_TRACKER_RESULT, paramMap);
                    db.execSQL("update " + TableEnum.TRACK_TARGET + " set LAST_CHECK_DATE=?1, LAST_CHECK_INDEX=?2, INVEST_AMT=?3 where ID=?4", TypeUtil.dateToStr(new Date(), DateUtil.dateFormat_yyyyMMddHHmmss), String.valueOf(newIndex), newTl.getAmt().toString(), String.valueOf(tt.getId()));
                    hasAnyNotify = true;
                } else {
                    db.execSQL("update " + TableEnum.TRACK_TARGET + " set LAST_CHECK_DATE=?1, LAST_CHECK_INDEX=?2 where ID=?3", TypeUtil.dateToStr(new Date(), DateUtil.dateFormat_yyyyMMddHHmmss), String.valueOf(newIndex), String.valueOf(tt.getId()));
                }
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "check track target fail, targetName[" + tt.getTargetName() + "]", e);
                if (notUtil == null) {
                    notUtil = new NotificationUtil(ctx, "CheckIdxProcessor-1", "CheckIdxProcessorChannel");
                }
                String errLog = TypeUtil.exceptionToString(e);
                paramMap.put("indexCheckLog", errLog.substring(0, Math.min(200, errLog.length())));
                notUtil.addNotification("Check Target Invest Amt Failed", "targetName[" + tt.getTargetName() + "]\r\n" + e.getMessage(), Const._ACTION_INDEX_TRACKER_RESULT, paramMap);
                hasAnyNotify = true;
            }
        }
        if (!hasAnyNotify) {
            if (notUtil == null) {
                notUtil = new NotificationUtil(ctx, "CheckIdxProcessor-1", "CheckIdxProcessorChannel");
            }
            notUtil.addNotification("Check Target Invest Amt Finish", "all target check finish.\r\nNo target need to change invest amt");
        }
    }

    private void cleanDataFileDir() {
        File dwnDir = new File(Const._APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER);
        if (!dwnDir.exists()) {
            dwnDir.mkdir();
        }
        for (File file : dwnDir.listFiles()) {
            file.delete();
        }
    }
}
