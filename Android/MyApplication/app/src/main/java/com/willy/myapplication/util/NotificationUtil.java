package com.willy.myapplication.util;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.willy.myapplication.R;
import com.willy.myapplication.sequence.SequenceGenerator;

import java.util.HashMap;
import java.util.Map;

public class NotificationUtil {
    private String channelID;
    private String channelName;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuildFinish;
    private Context ctx;
    private SequenceGenerator seqGen = new SequenceGenerator();
    public NotificationUtil(Context ctx, String channelID, String channelName) {
        this.ctx = ctx;
        this.channelID = channelID;
        this.channelName = channelName;
        createChannel();
    }
    private void createChannel() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(channel);
        }
    }
    public NotificationManager getManager() {
        NotificationManager mNManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        return mNManager;
    }
    public NotificationCompat.Builder notificationChannelBuild(String title, String message){
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, seqGen.getNext(),new Intent(ctx, ctx.getClass()),PendingIntent.FLAG_IMMUTABLE);
        return notificationBuildFinish = new NotificationCompat.Builder(ctx.getApplicationContext(),channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder notificationChannelBuild(String title, String message, String action, Map<String, String> paramMap){
        Intent it = new Intent(action);
        paramMap.forEach((k, v) -> it.putExtra(k, v));
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, seqGen.getNext(), it,PendingIntent.FLAG_IMMUTABLE);
        return notificationBuildFinish = new NotificationCompat.Builder(ctx.getApplicationContext(),channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public void putNotification(int id, String title, String message) {
        this.getManager().notify(id, notificationChannelBuild(title, message).build());
    }
    public void addNotification(String title, String message) {
        this.getManager().notify(this.getManager().getActiveNotifications().length, notificationChannelBuild(title, message).build());
    }
    public void addNotification(String title, String message, String action, Map<String, String> paramMap) {
        this.getManager().notify(this.getManager().getActiveNotifications().length, notificationChannelBuild(title, message, action, paramMap).build());
    }
    public void clearAllNotification() {
        this.getManager().cancelAll();
    }
}
