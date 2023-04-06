package com.willy.myapplication.listener.CalculateInvestPlan;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.util.DBUtil;

public class ImportBtnListener implements View.OnClickListener {
    private Context ctx;
    private AppCompatActivity activity;

    public ImportBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (AppCompatActivity) ctx;
    }

    @Override
    public void onClick(View view) {
        String targetNameTxt = ((EditText) activity.findViewById(R.id.targetNameTxt)).getText().toString();
        String targetCodeTxt = ((EditText) activity.findViewById(R.id.targetCodeTxt)).getText().toString();
        String market = ((Spinner) activity.findViewById(R.id.marketSpinner)).getSelectedItem().toString();

        String invPlanStr = ((TextView) this.activity.findViewById(R.id.calResult)).getText().toString();


        DBUtil dbutil = new DBUtil(this.ctx);

//        dbutil.execSQL("INSERT INTO TRACK_TARGET VALUES ('" + targetNameTxt + "', '" + market + "', null, '19700101', 0, 0)");
        String codeColumn;
        if(market.equals("TWSE")) {
            codeColumn = "Code";
        }else if (market.equals("TPEX")){
            codeColumn = "SecuritiesCompanyCode";
        } else {
            throw new IllegalArgumentException("market is not in ('TWSE','TPEX')");
        }

        Cursor c = dbutil.query("select max(id) from TRACK_TARGET");
        c.moveToFirst();
        Integer trackTargetId = c.getInt(0) + 1;

        dbutil.execSQL("INSERT INTO TRACK_TARGET VALUES ("+ trackTargetId +", '" + targetNameTxt + "', '" + market + "', '{\""+codeColumn+"\" : \""+targetCodeTxt+"\"}', '19700101', 0, 0)");
//        System.out.println("INSERT INTO TRACK_TARGET VALUES ('" + targetNameTxt + "', '" + market + "', '{\""+codeColumn+"\" : \""+targetCodeTxt+"\"}', '19700101', 0, 0)");


        c = dbutil.query("select max(id) from TRACK_LIST");
        c.moveToFirst();
        Integer trackListId = c.getInt(0) + 1;
        for (String row : invPlanStr.split("\r\n")) {
            String[] rowData = row.split("-");
            dbutil.execSQL("insert into TRACK_LIST values("+trackListId+", " + trackTargetId + ",'" + rowData[0] + "','" + rowData[1] + "','" + rowData[2] + "');");
            trackListId++;
//            System.out.println("insert into TRACK_LIST values(" + (maxId + 1) + ",'" + rowData[0] + "','" + rowData[1] + "','" + rowData[2] + "');");
        }

    }
}
