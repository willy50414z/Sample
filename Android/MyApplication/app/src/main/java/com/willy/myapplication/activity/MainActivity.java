package com.willy.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.willy.myapplication.R;
import com.willy.myapplication.job.CheckInvestAmtJob;
import com.willy.myapplication.job.ShowNotificationJob;
import com.willy.myapplication.processor.CheckIdxProcessor;
import com.willy.myapplication.scheduler.PeriodicJobScheduler;
import com.willy.myapplication.util.NotificationUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> aAdapter;
    List<String> data;

    private static final int REQUEST_CODE = 12345;
    private PendingIntent pendingIntent;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMenuList();
//        registerService();

        if(!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            goToSettingPage();
        }

        PeriodicJobScheduler jobScheduler = new PeriodicJobScheduler(this);
        jobScheduler.scheduleJobs();

        // 设置闹钟
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        scheduleJob(14,0,0, 9001, CheckInvestAmtJob.class);
        scheduleJob(21,0,0, 9005, ShowNotificationJobs.class);
        scheduleJob(23,0,0, 9006, ShowNotificationJobs.class);
        scheduleJob(10,0,0, 9007, ShowNotificationJobs.class);
        scheduleJob(12,0,0, 9008, ShowNotificationJobs.class);
        scheduleJob(14,0,0, 9009, ShowNotificationJobs.class);
        scheduleJob(16,0,0, 9010, ShowNotificationJobs.class);
        scheduleJob(18,0,0, 9011, ShowNotificationJobs.class);
        scheduleJob(20,0,0, 9012, ShowNotificationJobs.class);
    }

    public static class ShowNotificationJobs extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationUtil nu = new NotificationUtil(context, "showNotificationId", "showNotificationName");
            nu.addNotification("showNotification", "Notifytime[" + new Date() + "]");
        }
    }

    private void scheduleJob(int hourOfDay, int minute, int second, int reqCode, Class<?> jobClazz) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        Intent intent = new Intent(this, jobClazz);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reqCode, intent, PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationUtil nu = new NotificationUtil(context, "alarmId", "alarmName");
            nu.addNotification("alarmTitle1", "alarmNotifi1, date["+new Date()+"]");
            CheckIdxProcessor cp = new CheckIdxProcessor(context);
            cp.check();
        }
    }

    private void createMenuList() {
        listView = (ListView) findViewById(R.id.testLv);

        //組織ListView中的資料
        data = new ArrayList<String>();
        data.add("切換Activity");
        data.add("Database功能展示");
        data.add("Index Tracker Setting");
        data.add("Index Tracker Result");
        data.add("Notification");
        data.add("Log Viewer");
        data.add("Download File");
        data.add("Calculate Invest Plan");
        for(int i=0;i<20;i++){
            data.add("Item"+i);
        }

        //取用系統內建layout
        int layoutId = android.R.layout.simple_list_item_1;

        aAdapter = new ArrayAdapter<String>(this, layoutId, data);

        listView.setAdapter(aAdapter);

        listView.setOnItemClickListener(new ListViewEventListener(this));
    }

    private void registerService() {
//        Intent intent = new Intent(MainActivity.this, SampleService.class);
//        startService(intent);

//        Intent intent = new Intent(MainActivity.this, IndexTrackerService.class);
//        startService(intent);
    }

//    public static class AlarmReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("tag", "alarm message" + new Date());
//            NotificationUtil n = new NotificationUtil(context, "channelId", "channelName");
//            n.addNotification("title", "messages" + new Date());
//        }
//    }

    private void goToSettingPage() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {// android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {//其它
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class ListViewEventListener implements AdapterView.OnItemClickListener{
        MainActivity superClass;
        public ListViewEventListener(MainActivity m) {
            this.superClass = m;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent its;
            Log.d(this.getClass().getSimpleName(), "position["+position+"]Value["+data.get(position)+"]");
            switch(position){
                case 0:
                    its = new Intent("android.intent.action.SECOND");
                    //set parameter for next activity
                    its.putExtra("name", "Willy");
                    its.putExtra("age", "27");

                    //assemble parameters to a bundle for transfer
                    Bundle bundle = new Bundle();
                    bundle.putString("b1", "b1value");
                    bundle.putString("b2", "b2value");
                    its.putExtras(bundle);

                    this.superClass.startActivity(its);
                    break;
                case 1:
                    its = new Intent();
                    its.setClass(MainActivity.this, SecondActivity.class);
//                    Intent it = new Intent("android.intent.action.DATABASE");
                    this.superClass.startActivity(its);
                    break;
                case 2:
                    its = new Intent();
                    its.setClass(MainActivity.this, IndexTrackerSettingActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 3:
                    its = new Intent();
                    its.setClass(MainActivity.this, IndexTrackerCheckResultActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 4:
                    its = new Intent();
                    its.setClass(MainActivity.this, NotificationActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 5:
                    its = new Intent();
                    its.setClass(MainActivity.this, LogViewerActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 6:
                    its = new Intent();
                    its.setClass(MainActivity.this, DownloadFileActivity.class);
                    this.superClass.startActivity(its);
                    break;
                case 7:
                    its = new Intent();
                    its.setClass(MainActivity.this, CalculateInvestPlanActivity.class);
                    this.superClass.startActivity(its);
                    break;
//                case 5:
//                    its = new Intent("android.intent.action.IndexTrackerResult");
//                    //set parameter for next activity
//                    its.putExtra("indexCheckLog", "indexCheckLog333");
//                    this.superClass.startActivity(its);
//                    break;
            }
        }
    }
}