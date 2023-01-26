package com.willy.myapplication.listener.notification;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.app.NotificationCompat;

import com.willy.myapplication.R;
import com.willy.myapplication.util.NotificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class NotifyBtnListener implements View.OnClickListener {
    private Context ctx;
    private Activity activity;

    public NotifyBtnListener(Context ctx) {
        this.ctx = ctx;
        this.activity = (Activity) this.ctx;
    }

    @Override
    public void onClick(View view) {
        String channelId = ((EditText)this.activity.findViewById(R.id.channelId)).getText().toString();
        String channelName = ((EditText)this.activity.findViewById(R.id.channelName)).getText().toString();
        Integer notifyId = Integer.valueOf(((EditText)this.activity.findViewById(R.id.notifyId)).getText().toString());
        String title = ((EditText)this.activity.findViewById(R.id.notifyTitle)).getText().toString();
        String message = ((EditText)this.activity.findViewById(R.id.notifyMessage)).getText().toString();
        String action = ((EditText)this.activity.findViewById(R.id.notifyActionEt)).getText().toString();
        String intentMsg = ((EditText)this.activity.findViewById(R.id.intentMsgEt)).getText().toString();
        String intentTxt = ((Spinner)this.activity.findViewById(R.id.intentSpinner)).getSelectedItem().toString();
        NotificationUtil notifUtil = new NotificationUtil(ctx, channelId, channelName);
        notifUtil.putNotification(notifyId, title, message);


        Intent it = new Intent(action);
        try {
            JSONObject intentData = new JSONObject(intentMsg);
            Iterator<String> keys = intentData.keys();
            while(keys.hasNext()){
                String key = keys.next();
                it.putExtra(key, intentData.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        int pendingIntentType = intentTxt.equals("FLAG_MUTABLE") ? PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_IMMUTABLE;

        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notifyId, it, pendingIntentType);
        notifUtil.getManager().notify(notifyId, new NotificationCompat.Builder(ctx.getApplicationContext(),channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).build());
    }
}
