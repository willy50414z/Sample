package com.willy.myapplication.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeUtil {
    public static String dateToStr(Date date) {
        return new SimpleDateFormat(DateUtil.dateFormat_yyyyMMdd).format(date);
    }

    public static String dateToStr(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date strToDate(String strDate) {
        String dateFormat = "";
        if (strDate.matches("[0-9]{8}")) {
            dateFormat = DateUtil.dateFormat_yyyyMMdd;
        } else if (strDate.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")) {
            dateFormat = DateUtil.dateFormat_yyyyMMdd_Dash;
        } else if (strDate.matches("[0-9]{4}[/][0-9]{2}[/][0-9]{2}")) {
            dateFormat = DateUtil.dateFormat_yyyyMMdd_Slash;
        } else if (strDate.matches("[0-9]{14}")) {
            dateFormat = DateUtil.dateFormat_yyyyMMddHHmmss;
        } else if (strDate.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")) {
            dateFormat = DateUtil.dateFormat_DateTime_Dash;
        } else if (strDate.matches(
                "[0-9]{4}[-][0-9]{2}[-][0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]*")) {
            dateFormat = DateUtil.dateFormat_Timestamp;
        } else if (strDate.matches("[0-9]{8} [0-9]{2}:[0-9]{2}:[0-9]{2}")) {
            dateFormat = "yyyyMMdd hh:mm:ss";
        } else {
            Log.e(TypeUtil.class.getSimpleName(), "未指定日期格式");
            return null;
        }
        return strToDate(strDate, dateFormat);
    }

    public static Date strToDate(String strDate, String format) {
        try {
            return new SimpleDateFormat(format).parse(strDate);
        } catch (ParseException e) {
            Log.e(TypeUtil.class.getSimpleName(), "parse date fail, strDate[" + strDate + "]format[" + format + "]", e);
            return null;
        }
    }

    public static String exceptionToString(Exception e) {
        StringBuffer errorMsg = new StringBuffer(e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        errorMsg.append("\r\n");
        for (int i = 0; i < trace.length; i++) {
            errorMsg.append(trace[i]).append("\r\n");
        }
        return errorMsg.toString();
    }
}
