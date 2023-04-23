package com.willy.myapplication.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.willy.myapplication.processor.CheckIdxProcessor;

public class CheckInvestAmtJob extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new CheckIdxProcessor(context).check();
    }
}
