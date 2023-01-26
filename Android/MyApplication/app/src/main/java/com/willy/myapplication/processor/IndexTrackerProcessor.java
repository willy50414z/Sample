package com.willy.myapplication.processor;

import android.content.Context;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.adapter.TrackingItemAdapter;
import com.willy.myapplication.parser.TrackListParser;
import com.willy.myapplication.po.TrackListPO;
import com.willy.myapplication.util.DBUtil;

import java.util.Comparator;
import java.util.List;

public class IndexTrackerProcessor {
    private Context ctx;

    public IndexTrackerProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public void refreshTrackItemsList() {
        DBUtil dbUtil = new DBUtil(ctx);
        List<TrackListPO> tlList = dbUtil.<TrackListPO>query("select * from TRACK_LIST", new TrackListParser());
        tlList.sort(new Comparator<TrackListPO>() {
            @Override
            public int compare(TrackListPO t0, TrackListPO t1) {
                if (t0.getTrackTarget() != t1.getTrackTarget()) {
                    return Integer.compare(t0.getTrackTarget(), t1.getTrackTarget());
                } else if (!t0.getDnLimit().equals(t1.getDnLimit())) {
                    return t0.getDnLimit().compareTo(t1.getDnLimit());
                } else {
                    return 0;
                }
            }
        });
        TrackingItemAdapter mAdapter = new TrackingItemAdapter(ctx, tlList);
        ((ListView) ((AppCompatActivity) ctx).findViewById(R.id.trackingList)).setAdapter(mAdapter);
    }
}
