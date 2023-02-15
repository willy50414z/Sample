package com.willy.myapplication.constant;

import android.os.Environment;

public class Const {
    public final static String _DEFAULT_ENCODE = "UTF-8";
    public final static String _DEFAULT_REQUEST_METHOD = "GET";
    public final static String _REQUEST_METHOD_GET = "GET";
    public final static String _REQUEST_METHOD_POST = "POST";
    public final static String _APP_DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public final static String _APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER = _APP_DOWNLOAD_DIR + "/INDEX_TRACKER/";

    public final static String _PKG_NAME_PROCESSOR = "com.willy.myapplication.processor";

    public final static String _ACTION_INDEX_TRACKER_RESULT = "android.intent.action.IndexTrackerResult";

    public final static String _PATH_LOG_FILE = _APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER + "LOG/log.txt";
}
