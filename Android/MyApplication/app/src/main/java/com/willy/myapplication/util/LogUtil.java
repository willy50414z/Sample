package com.willy.myapplication.util;

import android.content.Context;

import com.willy.myapplication.constant.Const;

import java.io.IOException;
import java.util.Date;

public class LogUtil {
    public static void info(Context ctx, String message) {
        String logMessage = TypeUtil.dateToStr(new Date(), DateUtil.dateFormat_Timestamp) + "\t-\t" + message;
        try {
            FileUtil.writeByStr(ctx,Const._APP_DOWNLOAD_DIR , "log.txt", logMessage, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
