package com.willy.myapplication.listener.second;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.util.DBUtil;

public class UpdateBtnListener implements View.OnClickListener {
    private Context ctx;
    private AppCompatActivity activity;

    public UpdateBtnListener(Context context) {
        ctx = context;
        this.activity = ((AppCompatActivity) ctx);
    }

    @Override
    public void onClick(View view) {
        TableLayout mTableLayoutResult = activity.findViewById(R.id.table_layout_result);
        mTableLayoutResult = this.activity.findViewById(R.id.table_layout_result);
        mTableLayoutResult.removeAllViews();

        DBUtil db = new DBUtil(ctx);
        try {
            db.execSQL(((EditText) ((Activity) ctx).findViewById(R.id.edit_text_sql)).getText().toString());
            // 沒有查詢結果，顯示錯誤訊息
            TableRow row = new TableRow(this.ctx);
            TextView tv = new TextView(this.ctx);
            tv.setText("Msg");
            row.addView(tv);
            mTableLayoutResult.addView(row);

            row = new TableRow(this.ctx);
            tv = new TextView(this.ctx);
            tv.setText("update success");
            row.addView(tv);
            mTableLayoutResult.addView(row);
        } catch (Exception e) {
            // 沒有查詢結果，顯示錯誤訊息
            TableRow row = new TableRow(this.ctx);
            TextView tv = new TextView(this.ctx);
            tv.setText("errMsg");
            row.addView(tv);
            mTableLayoutResult.addView(row);

            row = new TableRow(this.ctx);
            tv = new TextView(this.ctx);
            tv.setMaxWidth(100);
            tv.setText(e.toString());
            row.addView(tv);
            mTableLayoutResult.addView(row);
        }

    }
}
