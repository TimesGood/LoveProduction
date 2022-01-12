package com.aige.loveproduction.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 手机端内置小型数据库
 */
public class OrderDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;//数据库版本
    private static final String DB_NAME = "Production.db";
    private static String TABLE_NAME = "";
    private static String CREATE_TABLE = "";
    //固定创建一个数据库以及版本
    public OrderDBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    public OrderDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    /**
     * 当数据库第一次创建时被调用
     * 作用：创建数据库、表 & 初始化数据
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + "(id integer primary key autoincrement,name varchar(64),address varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
