package com.willy.myapplication.processor;

import android.util.Log;

import com.willy.myapplication.constant.Const;
import com.willy.myapplication.util.FileUtil;
import com.willy.myapplication.util.HttpRequestUtil;
import com.willy.myapplication.util.TypeUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class FetchTAIEXProcessor extends FetchDataProcessor {

    @Override
    public Double getLastestIndex() throws Exception {
        //get new index
        String filePath = Const._APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER + "MI_5MINS_HIST_"+ new Date().getTime()+".json";
        File file = new File(filePath);
        if(!file.exists()) {
            HttpRequestUtil.download("https://openapi.twse.com.tw/v1/indicesReport/MI_5MINS_HIST", Const._REQUEST_METHOD_GET, filePath);
        }

        if (!file.exists()) {
            throw new FileNotFoundException("can't find downloaded file[" + filePath + "]");
        }
        String fileContent = FileUtil.readToString(filePath);
        if (fileContent.length() == 0) {
            throw new NullPointerException("the content in downloaded file[" + filePath + "] is empty");
        }
        JSONArray ja = null;
        try {
            ja = new JSONArray(fileContent);
            return ja.getJSONObject(ja.length() - 1).getDouble("ClosingIndex");
        } catch (JSONException e) {
            Log.e(this.getClass().getSimpleName(), "convert to json failed, fileContent[" + fileContent + "]", e);
            throw new JSONException("convert to json failed, fileContent[" + fileContent + "]");
        }
    }
}
