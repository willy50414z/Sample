package com.willy.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.indexTracker.AddTrackerBtnListener;
import com.willy.myapplication.listener.indexTracker.CheckTargetBtnListener;
import com.willy.myapplication.processor.IndexTrackerProcessor;
import com.willy.myapplication.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class IndexTrackerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_tracker);

        DBUtil dbUtil = new DBUtil(this);

        //set spinner values
        Cursor targetNames = dbUtil.query("select TARGET_NAME FROM TRACK_TARGET");
        List<String> tagetNameList = new ArrayList<>();
        targetNames.moveToFirst();
        do{
            tagetNameList.add(targetNames.getString(0));
        }while(targetNames.moveToNext());
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagetNameList);
        Spinner targetSp = findViewById(R.id.targetSpinner);
        targetSp.setAdapter(targetAdapter);

        //set add tracker button event
        Button addTrackerBtn = findViewById(R.id.addTrackerBtn);
        addTrackerBtn.setOnClickListener(new AddTrackerBtnListener(this));

        Button checkIdxBtn = findViewById(R.id.checkIdxBtn);
        checkIdxBtn.setOnClickListener(new CheckTargetBtnListener(this));

        // Initialize adapter and set adapter to list view
        new IndexTrackerProcessor(this).refreshTrackItemsList();

        if(!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            goToSettingPage();
        }
        //
//        //
//        listView = (ListView) findViewById(R.id.testLv);
//
//        //組織ListView中的資料
//        data = new ArrayList<String>();
//        data.add("切換Activity");
//        data.add("Database功能展示");
//        data.add("Index Tracker");
//        for(int i=0;i<20;i++){
//            data.add("Item"+i);
//        }
//
//        //取用系統內建layout
//        int layoutId = android.R.layout.simple_list_item_1;
//
//        aAdapter = new ArrayAdapter<String>(this, layoutId, data);
//
//        listView.setAdapter(aAdapter);
//
//        listView.setOnItemClickListener(new ListViewEventListener(this));
//
//
//        //寫log
//        Log.d(this.getClass().getSimpleName(), "testDLog");
//
//        //Toast
//        Toast.makeText(this, "show toast while start app", Toast.LENGTH_LONG).show();

    }

    private void goToSettingPage() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {// android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {//其它
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}