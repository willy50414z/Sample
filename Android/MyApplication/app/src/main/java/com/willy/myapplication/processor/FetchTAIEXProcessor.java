package com.willy.myapplication.processor;

import android.os.Environment;
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
        String fileName = "TAIEX_"+TypeUtil.dateToStr(new Date(), "yyyyMMdd")+".json";;
        HttpRequestUtil.downloadFile("https://openapi.twse.com.tw/v1/indicesReport/MI_5MINS_HIST", fileDir, fileName);
        File file = new File(this.fileDir, fileName);

        long start = new Date().getTime();
        while(!file.exists() && new Date().getTime() - start < 30000) {
            Thread.sleep(500);
        }

        if (!file.exists()) {
            throw new FileNotFoundException("can't find downloaded file[" + file.getAbsolutePath() + "]");
        }
        String fileContent = FileUtil.readToString(file.getAbsolutePath());
        if (fileContent.length() == 0) {
            throw new NullPointerException("the content in downloaded file[" + file.getAbsolutePath() + "] is empty");
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
