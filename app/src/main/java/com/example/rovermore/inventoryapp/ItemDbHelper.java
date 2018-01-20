package com.example.rovermore.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robertomoreno on 20/1/18.
 */

public class ItemDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="items.db";

    public ItemDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
}
