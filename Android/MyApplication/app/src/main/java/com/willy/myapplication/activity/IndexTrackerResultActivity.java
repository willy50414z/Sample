package com.willy.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;

public class IndexTrackerResultActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_tracker_result);

        Intent it = getIntent();
        TextView tv = (TextView) findViewById(R.id.indexCheckLog);
        tv.setText("");
        if(it.getStringExtra("indexCheckLog") != null) {
            tv.setText(it.getStringExtra("indexCheckLog"));
        }
    }
}
