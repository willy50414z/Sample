package com.willy.myapplication.parser;

import android.database.Cursor;

import com.willy.myapplication.po.TrackListPO;
import com.willy.myapplication.po.TrackTargetPO;
import com.willy.myapplication.util.TypeUtil;

import java.math.BigDecimal;

public class TrackTargetParser implements CursorParser{
    @Override
    public TrackTargetPO parse(Cursor c) {
        TrackTargetPO tt = new TrackTargetPO();
        tt.setId(c.getInt(0));
        tt.setTargetName(c.getString(1));
        tt.setTargetProc(c.getString(2));
        tt.setTargetProcArgs(c.getString(3));
        tt.setLastCheckDate(TypeUtil.strToDate(c.getString(4)));
        tt.setLastCheckIndex(c.getDouble(5));
        return tt;
    }
}
