package com.willy.myapplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.myapplication.R;
import com.willy.myapplication.parser.CursorParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBUtil extends SQLiteOpenHelper {
    private Context context;
    private final static int _DBVersion = 1;
    private final static String _DBName = "SampleList.db";
    public DBUtil(Context context) {
        super(context, _DBName, null, _DBVersion);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String[] initSqlAr = this.context.getResources().getStringArray(R.array.initSql);
        for(String sql : initSqlAr) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String[] updateSql = this.context.getResources().getStringArray(R.array.updateSql);
        for (String sql : updateSql) {
            sqLiteDatabase.execSQL(sql);
        }
    }

    public long insert(String tableName, List<Map<String, String>> valueList){
        SQLiteDatabase wdb = this.getWritableDatabase();
        ContentValues values;
        long successCount = 0;
        for (Map<String, String> valuesMap : valueList) {
            values = new ContentValues();
            for (String key : valuesMap.keySet()) {
                values.put(key, valuesMap.get(key));
            }
            successCount += wdb.insert(tableName, null, values);
        }
        return successCount;
    }

    public void execSQL(String sql, String... args){
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.execSQL(sql, args);
    }

    public void printAllTableData(String table) {
        Cursor c=this.getReadableDatabase().rawQuery("SELECT * FROM " + table, null);    // 查詢tb_name資料表中的所有資料
        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                StringBuilder row = new StringBuilder();
                for(int i=0;i<c.getColumnCount();i++) {
                    row.append(c.getString(i)).append(" - ");
                }
                Log.i(this.getClass().getSimpleName(), row.toString());
            } while (c.moveToNext());    // 有一下筆就繼續迴圈
        }
    }

    public Cursor query(String script, String... args) {
        Cursor c=this.getReadableDatabase().rawQuery(script, args);
        return c;
    }

    public <T> List<T> query(String script, CursorParser cp, String... args) {
        Cursor c=this.getReadableDatabase().rawQuery(script, args);
        return parseResult(c, cp);
    }

    public <T> List<T> parseResult(Cursor c, CursorParser cp){
        List<T> result = new ArrayList<>();
        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                result.add(cp.parse(c));
            } while (c.moveToNext());    // 有一下筆就繼續迴圈
        }
        return result;
    }
}
