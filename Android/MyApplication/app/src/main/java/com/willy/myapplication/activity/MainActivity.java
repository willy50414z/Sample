package com.willy.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


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
        data.add("Index Tracker");
        data.add("Notification");
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
                    its.setClass(MainActivity.this, NotificationActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 4:
                    its = new Intent("android.intent.action.IndexTrackerResult");
                    //set parameter for next activity
                    its.putExtra("indexCheckLog", "indexCheckLog333");
                    this.superClass.startActivity(its);
                    break;
            }
        }
    }
}