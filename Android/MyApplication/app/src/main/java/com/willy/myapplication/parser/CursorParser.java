package com.willy.myapplication.parser;

import android.database.Cursor;

public interface CursorParser {
    <T> T parse(Cursor c);
}
