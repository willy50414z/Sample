package com.willy.myapplication.processor;

import android.util.Log;

import com.willy.myapplication.constant.Const;
import com.willy.myapplication.util.FileUtil;
import com.willy.myapplication.util.HttpRequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

public class FetchBTCTWDProcessor extends FetchDataProcessor {

    @Override
    public Double getLastestIndex() throws Exception {
        //get new index
        String priceData = HttpRequestUtil.request("https://ace.io/polarisex/oapi/v2/list/tradePrice");
        JSONObject js = new JSONObject(priceData);
        return js.getJSONObject("BTC/TWD").getDouble("last_price");
    }
}
