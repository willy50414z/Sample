package com.willy.myapplication.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.willy.myapplication.activity.MainActivity;

import java.util.Calendar;

public class PeriodicJobScheduler {

    private Context ctx;

    public PeriodicJobScheduler(Context ctx) {
        this.ctx = ctx;
    }

    public void scheduleJobs() {
        scheduleJob(14,0,0, 9001, CheckInvestAmtJob.class);
    }

    private void scheduleJob(int hourOfDay, int minute, int second, int reqCode, Class<?> jobClazz) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        Intent intent = new Intent(ctx, jobClazz);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, reqCode, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
