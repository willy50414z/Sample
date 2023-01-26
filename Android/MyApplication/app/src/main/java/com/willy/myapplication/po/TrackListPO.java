package com.willy.myapplication.po;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.willy.myapplication.parser.TrackTargetParser;
import com.willy.myapplication.util.DBUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrackListPO {
    private int id;
    private int trackTarget;
    private BigDecimal upLimit;
    private BigDecimal dnLimit;
    private BigDecimal amt;

    public static List<TrackListPO> parse(Cursor c){
        TrackListPO tl;
        List<TrackListPO> result = new ArrayList<>();
        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                tl = new TrackListPO();
                tl.setId(c.getInt(0));
                tl.setTrackTarget(c.getInt(1));
                tl.setUpLimit(new BigDecimal(c.getInt(2)));
                tl.setDnLimit(new BigDecimal(c.getInt(3)));
                tl.setAmt(new BigDecimal(c.getInt(4)));
                result.add(tl);
            } while (c.moveToNext());
        }
        return result;
    }

    public TrackTargetPO getTrackTarget(Context cxt){
        DBUtil db = new DBUtil(cxt);
        Cursor c = db.query("select * from TRACK_TARGET where ID = ?1", String.valueOf(this.getTrackTarget()));
        List<TrackTargetPO> ttList = db.<TrackTargetPO>parseResult(c, new TrackTargetParser());
        return ttList.get(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackTarget() {
        return trackTarget;
    }

    public void setTrackTarget(int trackTarget) {
        this.trackTarget = trackTarget;
    }

    public BigDecimal getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(BigDecimal upLimit) {
        this.upLimit = upLimit;
    }

    public BigDecimal getDnLimit() {
        return dnLimit;
    }

    public void setDnLimit(BigDecimal dnLimit) {
        this.dnLimit = dnLimit;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "TrackListPO{" +
                "id=" + id +
                ", trackTarget=" + trackTarget +
                ", upLimit=" + upLimit +
                ", dnLimit=" + dnLimit +
                ", amt=" + amt +
                '}';
    }
}
