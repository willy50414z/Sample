package com.willy.myapplication.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.logViewer.RefreshBtnListener;

import java.util.List;

public class LogViewerActivity  extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> aAdapter;
    List<String> data;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_viewer);
        ((Button)findViewById(R.id.refreshBtn)).setOnClickListener(new RefreshBtnListener(this));
    }
}
