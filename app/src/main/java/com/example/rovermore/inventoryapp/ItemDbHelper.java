package com.example.rovermore.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robertomoreno on 20/1/18.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA = ", ";
    private static final String DEFAULT_0= " DEFAULT 0";
    private static final String CREATE_SQL_ENTRIES =
            "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME +
                    "(" +
                    ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
                    ItemContract.ItemEntry.PRODUCT_NAME + TEXT_TYPE + NOT_NULL + COMMA +
                    ItemContract.ItemEntry.QUANTITY + INTEGER_TYPE + DEFAULT_0 + COMMA +
                    ItemContract.ItemEntry.PRICE + INTEGER_TYPE + DEFAULT_0 + COMMA +
                    ItemContract.ItemEntry.MAIL + TEXT_TYPE +
                    ")";
    private static final String DELETE_SQL_ENTRIES =
            "DROP TABLE IF EXISTS" + ItemContract.ItemEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="items.db";

    public ItemDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SQL_ENTRIES);
        onCreate(db);
    }
}
