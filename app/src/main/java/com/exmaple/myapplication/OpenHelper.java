package com.exmaple.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class OpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_ARTICLE = "create table notes ("
            + "id integer primary key autoincrement, "
            + "title VARCHAR, "
            + "content VARCHAR, "
            + "type VARCHAR, "
            + "img VARCHAR, "
            + "time VARCHAR)";
    public OpenHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
