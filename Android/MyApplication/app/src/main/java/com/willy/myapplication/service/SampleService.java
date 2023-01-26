package com.willy.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SampleService extends Service {


    //建立服務
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(), "onCreate");
    }

    //綁定服務
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(this.getClass().getSimpleName(), "onBind");
        return null;
    }

    //啟動服務
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HelloService","onStartCommand Start");
        long endTime = System.currentTimeMillis() + 9*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
//                    wait(endTime - System.currentTimeMillis());
                    Log.d("HelloService","onStartCommand run");
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
            }
        }
        Log.d("HelloService","onStartCommand End");

        stopSelf();  // 停止Service

        return START_STICKY;
    }

    //解除綁定服務
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(this.getClass().getSimpleName(), "onUnbind");
        return super.onUnbind(intent);
    }

    //銷毀服務
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getSimpleName(), "onDestroy");
    }
}
