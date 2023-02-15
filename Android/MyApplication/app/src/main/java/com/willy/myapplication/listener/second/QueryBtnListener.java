package com.willy.myapplication.listener.second;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.willy.myapplication.R;
import com.willy.myapplication.util.DBUtil;

public class QueryBtnListener implements View.OnClickListener {
    private Context ctx;

    public QueryBtnListener(Context context) {
        ctx = context;
    }

    @Override
    public void onClick(View view) {
        DBUtil db = new DBUtil(ctx);
        Cursor c = db.query(((EditText)((Activity) ctx).findViewById(R.id.sqlEt)).getText().toString());
        StringBuilder row = new StringBuilder();
        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                for(int i=0;i<c.getColumnCount();i++) {
                    row.append(c.getString(i)).append(" - ");
                }
                row.append("\r\n");
            } while (c.moveToNext());    // 有一下筆就繼續迴圈
        }
        ((TextView)((Activity) ctx).findViewById(R.id.queryResult)).setText(row.toString());
    }
}
