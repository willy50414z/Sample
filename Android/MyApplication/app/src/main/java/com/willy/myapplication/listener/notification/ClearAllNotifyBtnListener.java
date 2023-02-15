package com.willy.myapplication.listener.notification;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.willy.myapplication.R;
import com.willy.myapplication.util.NotificationUtil;

public class ClearAllNotifyBtnListener implements View.OnClickListener {
    private Context ctx;
    private Activity activity;

    public ClearAllNotifyBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (Activity) this.ctx;
    }

    @Override
    public void onClick(View view) {
        String channelId = ((EditText)this.activity.findViewById(R.id.channelId)).getText().toString();
        String channelName = ((EditText)this.activity.findViewById(R.id.channelName)).getText().toString();
        NotificationUtil notifUtil = new NotificationUtil(ctx, channelId, channelName);
        notifUtil.clearAllNotification();
    }
}
