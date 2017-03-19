package com.rixin.cold.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 飘渺云轩 on 2017/3/15.
 */

public class ColdDBOpenHelper extends SQLiteOpenHelper {

    public ColdDBOpenHelper(Context context) {
        super(context, "rixin", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table cold (_id integer primary key autoincrement,title text unique,picUrl text,contentUrl,read integer,star integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
