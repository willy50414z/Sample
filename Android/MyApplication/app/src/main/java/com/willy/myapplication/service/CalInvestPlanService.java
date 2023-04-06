package com.willy.myapplication.service;

import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.dto.InvestPlanRowDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalInvestPlanService {
    public List<InvestPlanRowDto> calInvPlan(AppCompatActivity activity) {
        BigDecimal baseInvAmtNum = new BigDecimal(((EditText) activity.findViewById(R.id.baseInvAmtNum)).getText().toString());
        BigDecimal levelDiffAmtNum = new BigDecimal(((EditText) activity.findViewById(R.id.levelDiffAmtNum)).getText().toString());
        BigDecimal levelCountNum = new BigDecimal(((EditText) activity.findViewById(R.id.levelCountNum)).getText().toString());
        BigDecimal highNum = new BigDecimal(((EditText) activity.findViewById(R.id.highNum)).getText().toString());
        BigDecimal lowNum = new BigDecimal(((EditText) activity.findViewById(R.id.lowNum)).getText().toString());
        BigDecimal baseIdxNum = new BigDecimal(((EditText) activity.findViewById(R.id.baseIdxNum)).getText().toString());


        InvestPlanRowDto ip;
        List<InvestPlanRowDto> ipList = new ArrayList<>();
        for(int i = 0; i < levelCountNum.intValue(); i++) {
            int middleLevelStep = levelCountNum.intValue() / 2;
            BigDecimal levelAmt = baseInvAmtNum.add(levelDiffAmtNum.multiply(i == 0 ? new BigDecimal(0.5) : new BigDecimal(2).pow(i-1)));

            BigDecimal index;
            if(i<middleLevelStep) {
                index = baseIdxNum.add(highNum.subtract(baseIdxNum).divide(new BigDecimal(middleLevelStep - 1), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(middleLevelStep-i)));
            } else if (i == middleLevelStep) {
                index = baseIdxNum;
            }else if(levelCountNum.intValue() - 1 == i) {
                index = new BigDecimal(0);
            } else {
                index = baseIdxNum.subtract(baseIdxNum.subtract(lowNum).divide(new BigDecimal(middleLevelStep - 1), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(i - middleLevelStep)));
            }
            ipList.add(new InvestPlanRowDto(levelAmt, index));
        }
        return ipList;
    }
}
