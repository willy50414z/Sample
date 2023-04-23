package com.willy.myapplication.processor;

import android.os.Environment;

import org.json.JSONObject;

import java.io.File;

public abstract class FetchDataProcessor {
    String sidCode;
    final File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/IndexTracker");
    public abstract Double getLastestIndex() throws Exception;

    public void setSidCode(String sidCode) {
        this.sidCode = sidCode;
    }
}
