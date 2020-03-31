package com.example.myshop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myshop.data.itemContract.*;


public class shopDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = shopDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "shop.db";

    public static final int DATABASE_VERSION = 1 ;

    public shopDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_SHOP_TABLE = "CREATE TABLE " + itemEntry.TABLE_NAME + "("
                                + itemEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + itemEntry.COLUMN_ITEM_NAME + "TEXT NOT NULL ,"
                                + itemEntry.COLUMN_ITEM_PRICE + "INTEGER NOT NULL , "
                                + itemEntry.COLUMN_ITEM_QUANTITY + "INTEGER NOT NULL DEFAULT 0 ,"
                                + itemEntry.COLUMN_USER_REPORT + "TEXT ) ;";
        db.execSQL(SQL_CREATE_SHOP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // database is still at version 1 , so nothing to do here.....
    }
}
