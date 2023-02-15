package com.willy.myapplication.processor;

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
        String filePath = Const._APP_DOWNLOAD_FILE_DIR_INDEX_TRACKER + "tpex_mainboard_quotes_" + new Date().getTime() + ".json";
        File file = new File(filePath);
        if(!file.exists()) {
            HttpRequestUtil.download("https://www.tpex.org.tw/openapi/v1/tpex_mainboard_quotes", Const._REQUEST_METHOD_GET, filePath);
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
            String stockCodeKey = "SecuritiesCompanyCode";
            for (int i = 0; i < ja.length(); i++) {
                if (ja.getJSONObject(i).getString(stockCodeKey).equals(this.jsonFilter.getString(stockCodeKey))) {
                    return ja.getJSONObject(i).getDouble("Close");
                }
            }
            throw new NullPointerException("Can't get price data for stock code 00696B");
        } catch (Exception e) {
            throw new JSONException("fetch index data failed, fileContent[" + fileContent.substring(0, Math.min(100, fileContent.length())) + "]");
        }
    }
}
