package com.willy.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;


import com.willy.myapplication.R;
import com.willy.myapplication.service.IndexTrackerService;
import com.willy.myapplication.service.SampleService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> aAdapter;
    List<String> data;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        listView = (ListView) findViewById(R.id.testLv);

        //組織ListView中的資料
        data = new ArrayList<String>();
        data.add("切換Activity");
        data.add("Database功能展示");
        data.add("Index Tracker Setting");
        data.add("Index Tracker Result");
        data.add("Notification");
        data.add("Log Viewer");
        for(int i=0;i<20;i++){
            data.add("Item"+i);
        }

        //取用系統內建layout
        int layoutId = android.R.layout.simple_list_item_1;

        aAdapter = new ArrayAdapter<String>(this, layoutId, data);

        listView.setAdapter(aAdapter);

        listView.setOnItemClickListener(new ListViewEventListener(this));


        //寫log
        Log.d(this.getClass().getSimpleName(), "testDLog");

        //Toast
        Toast.makeText(this, "show toast while start app", Toast.LENGTH_LONG).show();


//        Intent intent = new Intent(MainActivity.this, SampleService.class);
//        startService(intent);

        Intent intent = new Intent(MainActivity.this, IndexTrackerService.class);
        startService(intent);

        if(!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            goToSettingPage();
        }
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

    private class ListViewEventListener implements AdapterView.OnItemClickListener{
        MainActivity superClass;
        public ListViewEventListener(MainActivity m) {
            this.superClass = m;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent its;
            Log.d(this.getClass().getSimpleName(), "position["+position+"]Value["+data.get(position)+"]");
            switch(position){
                case 0:
                    its = new Intent("android.intent.action.SECOND");
                    //set parameter for next activity
                    its.putExtra("name", "Willy");
                    its.putExtra("age", "27");

                    //assemble parameters to a bundle for transfer
                    Bundle bundle = new Bundle();
                    bundle.putString("b1", "b1value");
                    bundle.putString("b2", "b2value");
                    its.putExtras(bundle);

                    this.superClass.startActivity(its);
                    break;
                case 1:
                    its = new Intent();
                    its.setClass(MainActivity.this, SecondActivity.class);
//                    Intent it = new Intent("android.intent.action.DATABASE");
                    this.superClass.startActivity(its);
                    break;
                case 2:
                    its = new Intent();
                    its.setClass(MainActivity.this, IndexTrackerActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 3:
                    its = new Intent();
                    its.setClass(MainActivity.this, IndexTrackerCheckResult.class);
                    this.superClass.startActivity(its);
                    break;
                case 4:
                    its = new Intent();
                    its.setClass(MainActivity.this, NotificationActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 5:
                    its = new Intent();
                    its.setClass(MainActivity.this, LogViewerActivity.class);
                    this.superClass.startActivity(its);
                    break;
//                case 5:
//                    its = new Intent("android.intent.action.IndexTrackerResult");
//                    //set parameter for next activity
//                    its.putExtra("indexCheckLog", "indexCheckLog333");
//                    this.superClass.startActivity(its);
//                    break;
            }
        }
    }
}