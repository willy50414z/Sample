package com.willy.myapplication.listener.logViewer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.willy.myapplication.R;
import com.willy.myapplication.util.FileUtil;

public class RefreshBtnListener implements View.OnClickListener{
    private Context ctx;
    private Activity activity;

    public RefreshBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (Activity) ctx;
    }

    @Override
    public void onClick(View view) {
        String fileName = ((EditText)activity.findViewById(R.id.fileNameEt)).getEditableText().toString();
        TextView contentTv = ((TextView)activity.findViewById(R.id.contentTv));
        contentTv.setText(FileUtil.readToString(ctx.getFilesDir() + "/" + fileName));
    }
}
