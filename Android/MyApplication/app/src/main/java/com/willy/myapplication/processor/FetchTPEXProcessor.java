package com.willy.myapplication.processor;

import android.os.Environment;

import com.willy.myapplication.constant.Const;
import com.willy.myapplication.util.FileUtil;
import com.willy.myapplication.util.HttpRequestUtil;
import com.willy.myapplication.util.TypeUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class FetchTPEXProcessor extends FetchDataProcessor {

    @Override
    public Double getLastestIndex() throws Exception {
        //get new index
        String fileName = "TPEX_"+TypeUtil.dateToStr(new Date(), "yyyyMMdd")+".json";
        HttpRequestUtil.downloadFile("https://www.tpex.org.tw/openapi/v1/tpex_mainboard_quotes", fileDir, fileName);
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
            String stockCodeKey = "SecuritiesCompanyCode";
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString(stockCodeKey).equals(this.sidCode)) {
                    return ja.getJSONObject(i).getDouble("Close");
                }
            }
            throw new NullPointerException("Can't get price data for stock code 00696B");
        } catch (Exception e) {
            throw new JSONException("fetch index data failed, fileContent[" + fileContent.substring(0, Math.min(100, fileContent.length())) + "]");
        }
    }
}
