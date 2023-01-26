package com.willy.myapplication.listener.indexTracker;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.processor.IndexTrackerProcessor;
import com.willy.myapplication.util.DBUtil;

import java.util.Arrays;
import java.util.HashMap;

public class AddTrackerBtnListener implements View.OnClickListener {
    private Context ctx;
    private AppCompatActivity activity;
    private DBUtil dbUtil;

    public AddTrackerBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = ((AppCompatActivity) ctx);
        this.dbUtil = new DBUtil(ctx);
    }

    @Override
    public void onClick(View view) {
        Cursor trackTarget = dbUtil.query("select * from TRACK_TARGET where TARGET_NAME = ?1"
                , ((Spinner) activity.findViewById(R.id.targetSpinner)).getSelectedItem().toString());
        trackTarget.moveToFirst();
        HashMap<String, String> trackListValueMap = new HashMap<>();
        trackListValueMap.put("TRACK_TARGET", trackTarget.getString(0));
        trackListValueMap.put("UP_LIMIT", ((EditText) activity.findViewById(R.id.upLimitNum)).getText().toString());
        trackListValueMap.put("DN_LIMIT", ((EditText) activity.findViewById(R.id.dnLimitNum)).getText().toString());
        trackListValueMap.put("AMT", ((EditText) activity.findViewById(R.id.amtNum)).getText().toString());
        dbUtil.insert("TRACK_LIST", Arrays.asList(trackListValueMap));
        new IndexTrackerProcessor(this.ctx).refreshTrackItemsList();
    }
}
