package com.willy.myapplication.listener.CalculateInvestPlan;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.dto.InvestPlanRowDto;
import com.willy.myapplication.service.CalInvestPlanService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalBtnListener implements View.OnClickListener{
    private Context ctx;
    private AppCompatActivity activity;

    public CalBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (AppCompatActivity) ctx;
    }

    @Override
    public void onClick(View view) {

        CalInvestPlanService cpSvc = new CalInvestPlanService();
        List<InvestPlanRowDto> ipList = cpSvc.calInvPlan(this.activity);
        StringBuffer sb = new StringBuffer();
        for (int i=1;i<ipList.size();i++){
            sb.append(ipList.get(i-1).getIndex() + " - " + ipList.get(i).getIndex() + " - " + ipList.get(i).getAmt()).append("\r\n");
        }
        ((TextView)this.activity.findViewById(R.id.calResult)).setText(sb.toString());
    }
}
