package com.willy.myapplication.job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.willy.myapplication.processor.CheckIdxProcessor;
import com.willy.myapplication.util.NotificationUtil;

import java.util.Date;

public class ShowNotificationJob extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil nu = new NotificationUtil(context, "showNotificationId", "showNotificationName");
        nu.addNotification("showNotification", "Notifytime[" + new Date() + "]");
    }
}