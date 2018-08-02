package com.rixin.cold.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rixin.cold.domain.ColdInfo;

import java.util.ArrayList;

/**
 * 数据库操作工具类
 * Created by 飘渺云轩 on 2017/3/15.
 */

public class DBUtils {

    /**
     * 添加收藏
     *
     * @param title
     * @param picUrl
     * @param read
     * @return
     */
    public static boolean insert(ColdDBOpenHelper helper, String title, String picUrl, String contentUrl, int read) {
        SQLiteDatabase db = helper.getWritableDatabase();  // 打开读写的数据库
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("picUrl", picUrl);
        values.put("contentUrl", contentUrl);
        values.put("read", read);
        long i = db.insert("cold", null, values);
        db.close();
        if (i == -1) {
            return false;
        }
        return true;
    }

    /**
     * 取消收藏
     *
     * @param title
     * @return
     */
    public static boolean delete(ColdDBOpenHelper helper, String title) {
        SQLiteDatabase db = helper.getWritableDatabase(); // 打开读写的数据库
        int i = db.delete("cold", "title=?", new String[]{title});
        db.close();
        if (i == 0) {
            return false;
        }
        return true;
    }

    /**
     * 查询全部收藏
     *
     * @param helper
     * @return
     */
    public static ArrayList<ColdInfo> query(ColdDBOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();  // 打开只读的数据库
        // 得到结果集的游标
        Cursor cursor = db.rawQuery("select * from cold", null);
        ArrayList<ColdInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ColdInfo info = new ColdInfo();
            info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            info.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
            info.setContentUrl(cursor.getString(cursor.getColumnIndex("contentUrl")));
            info.setReadCount(cursor.getInt(cursor.getColumnIndex("read")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询当前内容是否已经收藏
     *
     * @param helper
     * @param title  以 title 为 key
     * @return
     */public
     static boolean query(ColdDBOpenHelper helper, String title) {
        SQLiteDatabase db = helper.getReadableDatabase();  // 打开一个只读的数据库
        Cursor cursor = db.query("cold", null, "title=?", new String[]{title}, null, null, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

}
