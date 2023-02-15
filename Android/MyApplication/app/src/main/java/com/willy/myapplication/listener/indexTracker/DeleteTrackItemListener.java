package com.willy.myapplication.listener.indexTracker;

import android.content.Context;
import android.view.View;

import com.willy.myapplication.processor.IndexTrackerProcessor;
import com.willy.myapplication.util.DBUtil;

public class DeleteTrackItemListener implements View.OnClickListener {
    private Context ctx;
    private DBUtil dbUtil;

    public DeleteTrackItemListener(Context ctx) {
        this.ctx = ctx;
        this.dbUtil = new DBUtil(this.ctx);
    }

    @Override
    public void onClick(View view) {
        dbUtil.execSQL("delete from TRACK_LIST where id=?1", String.valueOf(view.getId()));
        new IndexTrackerProcessor(ctx).refreshTrackItemsList();
    }
}
