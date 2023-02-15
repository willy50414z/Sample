package com.willy.myapplication.processor;

import android.app.Activity;
import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.willy.myapplication.R;
import com.willy.myapplication.parser.TrackTargetParser;
import com.willy.myapplication.po.TrackTargetPO;
import com.willy.myapplication.util.DBUtil;
import com.willy.myapplication.util.DateUtil;
import com.willy.myapplication.util.TypeUtil;

import java.util.List;

public class IndexTrackerCheckResultProcessor {
    private Context ctx;

    public IndexTrackerCheckResultProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public void refreshCheckResultTable(){
        DBUtil dbUtil = new DBUtil(this.ctx);

        List<TrackTargetPO> ttList = dbUtil.query("SELECT * FROM TRACK_TARGET", new TrackTargetParser());

        TableLayout tl = ((Activity) this.ctx).findViewById(R.id.checkTargetResultTableLo);
        tl.removeAllViews();

        //add title
        TextView tv;
        TableRow tr;
        int tvHeight = 180;
        int tableTvHeight = 80;

        tr = new TableRow(this.ctx);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tv = new TextView(this.ctx);
        tv.setText("標的");
        tv.setWidth(200);
        tv.setHeight(tableTvHeight);
        tr.addView(tv);

        tv = new TextView(this.ctx);
        tv.setText("檢測時間");
        tv.setWidth(350);
        tv.setHeight(tableTvHeight);
        tr.addView(tv);

        tv = new TextView(this.ctx);
        tv.setText("指數");
        tv.setWidth(300);
        tv.setHeight(tableTvHeight);
        tr.addView(tv);

        tv = new TextView(this.ctx);
        tv.setText("金額");
        tv.setWidth(300);
        tv.setHeight(tableTvHeight);
        tr.addView(tv);

        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        //add result
        for (TrackTargetPO tt : ttList) {
            tr = new TableRow(this.ctx);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tv = new TextView(this.ctx);
            tv.setText(tt.getTargetName());
            tv.setWidth(200);
            tv.setHeight(tvHeight);
            tr.addView(tv);

            tv = new TextView(this.ctx);
            tv.setText(TypeUtil.dateToStr(tt.getLastCheckDate(), DateUtil.dateFormat_DateTime_Dash));
            tv.setWidth(350);
            tv.setHeight(tvHeight);
            tr.addView(tv);

            tv = new TextView(this.ctx);
            tv.setText(String.valueOf(tt.getLastCheckIndex()));
            tv.setWidth(300);
            tv.setHeight(tvHeight);
            tr.addView(tv);

            tv = new TextView(this.ctx);
            tv.setText(String.valueOf(tt.getInvestAmt()));
            tv.setWidth(300);
            tv.setHeight(tvHeight);
            tr.addView(tv);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
