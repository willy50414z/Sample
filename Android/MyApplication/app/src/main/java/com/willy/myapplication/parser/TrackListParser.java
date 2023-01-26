package com.willy.myapplication.parser;

import android.database.Cursor;

import com.willy.myapplication.po.TrackListPO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TrackListParser implements CursorParser{
    @Override
    public TrackListPO parse(Cursor c) {
        TrackListPO tl = new TrackListPO();
        tl.setId(c.getInt(0));
        tl.setTrackTarget(c.getInt(1));
        tl.setUpLimit(new BigDecimal(c.getDouble(2)).setScale(2, RoundingMode.HALF_UP));
        tl.setDnLimit(new BigDecimal(c.getDouble(3)).setScale(2, RoundingMode.HALF_UP));
        tl.setAmt(new BigDecimal(c.getInt(4)));
        return tl;
    }
}
