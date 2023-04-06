package com.willy.myapplication.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.CalculateInvestPlan.CalBtnListener;
import com.willy.myapplication.listener.CalculateInvestPlan.ImportBtnListener;
import com.willy.myapplication.util.DBUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class CalculateInvestPlanActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calinvestplan);

        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList("TWSE", "TPEX"));
        Spinner targetSp = findViewById(R.id.marketSpinner);
        targetSp.setAdapter(targetAdapter);

        findViewById(R.id.calBtn).setOnClickListener(new CalBtnListener(this));
        findViewById(R.id.importBtn).setOnClickListener(new ImportBtnListener(this));
//        DBUtil dbUtil = new DBUtil(this);
    }
}
