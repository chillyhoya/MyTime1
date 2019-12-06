package com.exmaple.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class MemorialDao {

    public static final String DB_NAME = "memoria";

    public static final int VERSION = 1;

    private static MemorialDao userDB;

    private SQLiteDatabase db;

    private MemorialDao(Context context) {
        OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取SqliteDB实例
     * @param context
     */
    public synchronized static MemorialDao getInstance(Context context) {
        if (userDB == null) {
            userDB = new MemorialDao(context);
        }
        return userDB;
    }

    public ContentValues getContentValues(Memorial order){
        ContentValues cv = new ContentValues();
        cv.put("title", order.getTitle());
        cv.put("content", order.getContent());
        cv.put("time", order.getTime());
        cv.put("type", order.getType());
        cv.put("img", order.getImg());
        return cv;

    }


       public Boolean insertNote(Memorial order) {
        if (order != null) {
            long result = db.insert("notes", null, getContentValues(order));
            if (result != -1) {
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }
    public Boolean del(String id) {
        db.delete("notes", "id" + "=?" , new String[]{id});
        return  false;
    }
    public Boolean updateNote(Memorial order) {
        if (order != null) {
            long result = db.update("notes",  getContentValues(order),"id" + "=?" , new String[]{order.getId()+""});
            if (result != -1) {//-1代表添加失败
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }

    public List<Memorial> loadNote(){
        ArrayList<Memorial> orderList = new ArrayList<Memorial>();
        Cursor cursor = db.query("notes", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Memorial order = new Memorial(id,title, content, time,type,img);
                orderList.add(order);
            }

            cursor.close();
        }
        return orderList;
    }

}
