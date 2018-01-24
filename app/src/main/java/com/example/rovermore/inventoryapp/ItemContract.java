package com.example.rovermore.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by robertomoreno on 20/1/18.
 */

public final class ItemContract {

    private ItemContract(){}

    //Parses the new content URI into constants
    public static final String CONTENT_AUTHORITY = "com.example.rovermore.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";

    public static class ItemEntry implements BaseColumns{

        public static final String TABLE_NAME = "items";
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "name";
        public static final String QUANTITY = "quantity";
        public static final String PRICE = "price";
        public static final String MAIL="mail";

        //Constant to access the content URI for the table items
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + PATH_ITEMS);

        //The MIME type of the {@link #CONTENT_URI} for a list of items.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        //The MIME type of the {@link #CONTENT_URI} for a single item.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

    }





}
