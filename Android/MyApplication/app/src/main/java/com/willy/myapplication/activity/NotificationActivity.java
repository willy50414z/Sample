package com.willy.myapplication.activity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.willy.myapplication.R;
import com.willy.myapplication.listener.notification.NotifyBtnListener;

import java.util.Arrays;

public class NotificationActivity extends AppCompatActivity {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuildFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Button notifyBtn = (Button) findViewById(R.id.notifyBtn);
        notifyBtn.setOnClickListener(new NotifyBtnListener(this));
        Button clearAllNotificationBtn = (Button) findViewById(R.id.clearAllNotificationBtn);
        clearAllNotificationBtn.setOnClickListener(new NotifyBtnListener(this));

        String[] pendingIntentAr = getResources().getStringArray(R.array.pendingIntent);
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList(pendingIntentAr));
        Spinner targetSp = findViewById(R.id.intentSpinner);
        targetSp.setAdapter(targetAdapter);
    }
}
