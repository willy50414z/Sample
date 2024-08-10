package com.willy.myapplication.listener.second;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.util.DBUtil;

public class QueryBtnListener implements View.OnClickListener {
    private Context ctx;
    private AppCompatActivity activity;

    public QueryBtnListener(Context context) {
        this.ctx = context;
        this.activity = ((AppCompatActivity) ctx);
    }

    @Override
    public void onClick(View view) {
        DBUtil db = new DBUtil(ctx);

        EditText mEditTextSql = activity.findViewById(R.id.edit_text_sql);
        TableLayout mTableLayoutResult = activity.findViewById(R.id.table_layout_result);
        mTableLayoutResult.removeAllViews();
        // 從輸入框獲取 SQL 語句
        String sql = mEditTextSql.getText().toString().trim();
        if (!TextUtils.isEmpty(sql)) {
            try {
                // 執行 SQL 查詢
                Cursor cursor = db.query(sql, null);

                // 判斷是否有查詢結果
                if (cursor != null && cursor.getCount() > 0) {
                    // 列名數組
                    String[] columns = cursor.getColumnNames();

                    // 創建表頭
                    TableRow row = new TableRow(this.ctx);
                    for (String column : columns) {
                        TextView tv = new TextView(this.ctx);
                        tv.setText(column);
                        row.addView(tv);
                    }
                    mTableLayoutResult.addView(row);

                    // 遍歷結果集，顯示數據
                    while (cursor.moveToNext()) {
                        // 創建一行
                        TableRow dataRow = new TableRow(this.ctx);
                        for (String column : columns) {
                            if(cursor.getColumnIndex(column)<0) {
                                continue;
                            }
                            TextView tv = new TextView(this.ctx);
                            @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(column));
                            tv.setText(data);
                            dataRow.addView(tv);
                        }
                        mTableLayoutResult.addView(dataRow);
                    }

                    // 關閉游標
                    cursor.close();
                } else {
                    // 沒有查詢結果，顯示錯誤訊息
                    TableRow row = new TableRow(this.ctx);
                    TextView tv = new TextView(this.ctx);
                    tv.setText("errMsg");
                    row.addView(tv);
                    mTableLayoutResult.addView(row);

                    row = new TableRow(this.ctx);
                    tv = new TextView(this.ctx);
                    tv.setText("No Result");
                    row.addView(tv);
                    mTableLayoutResult.addView(row);
                }
            } catch (Exception e) {
                // 查詢失敗，顯示錯誤訊息
                TableRow row = new TableRow(this.ctx);
                TextView tv = new TextView(this.ctx);
                tv.setText("errMsg");
                row.addView(tv);
                mTableLayoutResult.addView(row);

                row = new TableRow(this.ctx);
                tv = new TextView(this.ctx);
                tv.setText(e.getMessage());
                tv.setMaxWidth(100);
                row.addView(tv);
                mTableLayoutResult.addView(row);
            }
            // 關閉數據庫
            db.close();
        }
    }
}
