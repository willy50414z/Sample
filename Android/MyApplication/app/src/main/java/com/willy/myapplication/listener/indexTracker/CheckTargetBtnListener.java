package com.willy.myapplication.listener.indexTracker;

import android.content.Context;
import android.view.View;

import com.willy.myapplication.processor.CheckIdxProcessor;
import com.willy.myapplication.processor.IndexTrackerCheckResultProcessor;

public class CheckTargetBtnListener implements View.OnClickListener{
    private final Context ctx;

    public CheckTargetBtnListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onClick(View view) {
        CheckIdxProcessor cp = new CheckIdxProcessor(ctx);
        cp.check();

        new IndexTrackerCheckResultProcessor(ctx).refreshCheckResultTable();
    }
}
