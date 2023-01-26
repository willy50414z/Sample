package com.willy.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.second.QueryBtnListener;
import com.willy.myapplication.listener.second.UpdateBtnListener;

import org.jetbrains.annotations.Nullable;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        
//        //assamble all parameters from last activity
//        Intent te = getIntent();
//        StringBuffer paramsSb = new StringBuffer();
//        paramsSb.append("name=").append(te.getStringExtra("name")).append("\r\n");
//        paramsSb.append("age=").append(te.getStringExtra("age")).append("\r\n");
//
//        Bundle bu = te.getExtras();
//        paramsSb.append("b1=").append(bu.getString("b1")).append("\r\n");
//        paramsSb.append("b2=").append(bu.getString("b2")).append("\r\n");

        findViewById(R.id.queryBtn).setOnClickListener(new QueryBtnListener(this));
        findViewById(R.id.updateBtn).setOnClickListener(new UpdateBtnListener(this));
    }
}
