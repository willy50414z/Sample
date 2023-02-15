package com.willy.myapplication.listener.second;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.willy.myapplication.R;
import com.willy.myapplication.util.DBUtil;

public class UpdateBtnListener implements View.OnClickListener {
    private Context ctx;

    public UpdateBtnListener(Context context) {
        ctx = context;
    }

    @Override
    public void onClick(View view) {
        DBUtil db = new DBUtil(ctx);
        db.execSQL(((EditText)((Activity) ctx).findViewById(R.id.sqlEt)).getText().toString());
    }
}
