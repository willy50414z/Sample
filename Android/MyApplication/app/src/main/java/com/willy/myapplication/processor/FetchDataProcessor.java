package com.willy.myapplication.processor;

import org.json.JSONObject;

public abstract class FetchDataProcessor {
    JSONObject jsonFilter;
    public abstract Double getLastestIndex() throws Exception;
    public void setJsonFilter(JSONObject jsonFilter) {
        this.jsonFilter = jsonFilter;
    }
}
