package com.willy.myapplication.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.indexTracker.CheckTargetBtnListener;
import com.willy.myapplication.processor.IndexTrackerCheckResultProcessor;

public class IndexTrackerCheckResult extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_tracker_check_result);

        new IndexTrackerCheckResultProcessor(this).refreshCheckResultTable();

        Button checkIdxBtn = findViewById(R.id.checkIndex);
        checkIdxBtn.setOnClickListener(new CheckTargetBtnListener(this));
    }
}
