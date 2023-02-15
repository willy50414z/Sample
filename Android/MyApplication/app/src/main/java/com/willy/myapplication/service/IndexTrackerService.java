package com.willy.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.willy.myapplication.processor.CheckIdxProcessor;
import com.willy.myapplication.util.DBUtil;
import com.willy.myapplication.util.DateUtil;
import com.willy.myapplication.util.LogUtil;
import com.willy.myapplication.util.TypeUtil;

import java.util.Date;

public class IndexTrackerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBUtil db = new DBUtil(this);
        CheckIdxProcessor cp = new CheckIdxProcessor(this);
        new Thread(() -> {
            while(true) {
                //check should it executed
                Cursor c = db.query("select min(LAST_CHECK_DATE) from TRACK_TARGET");
                c.moveToFirst();
                if (!c.getString(0).substring(0, 8).equals(TypeUtil.dateToStr(new Date(), DateUtil.dateFormat_yyyyMMdd))
                        && TypeUtil.dateToStr(new Date(), "HH").equals("18")) {
                    cp.check();
                    Log.d(this.getClass().getSimpleName(), "lastCheckTime[" + c.getString(0) + "] run job");
                } else {
                    try {
                        //wait 1 hour
                        Log.d(this.getClass().getSimpleName(), "lastCheckTime[" + c.getString(0) + "] sleep 1 hour then check again");
                        LogUtil.info(this, "IndexTrackerService sleep");
                        Thread.sleep(1000 * 60 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogUtil.info(this, "IndexTrackerService error["+TypeUtil.exceptionToString(e)+"]");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.info(this, "IndexTrackerService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.info(this, "IndexTrackerService onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.info(this, "IndexTrackerService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogUtil.info(this, "IndexTrackerService onRebind");
    }
}
