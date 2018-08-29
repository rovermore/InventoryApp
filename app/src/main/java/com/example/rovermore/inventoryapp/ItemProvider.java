package com.example.rovermore.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by robertomoreno on 21/1/18.
 */

public class ItemProvider extends ContentProvider {

    public static final String LOG_TAG = ItemProvider.class.getSimpleName();

    private static final int ITEMS = 101;
    private static final int ITEMS_ID = 102;

    private ItemDbHelper itemDbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEMS_ID);
    }


    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {

        itemDbHelper = new ItemDbHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = itemDbHelper.getReadableDatabase();

        Cursor cursor;

        final int match = uriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                //query the items table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = db.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case ITEMS_ID:

                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }

    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (values.containsKey(ItemContract.ItemEntry.PRODUCT_NAME)) {
            String name = values.getAsString(ItemContract.ItemEntry.PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires name");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.QUANTITY)) {
            Integer quantity = values.getAsInteger(ItemContract.ItemEntry.QUANTITY);
            if (quantity < 0) {
                throw new IllegalArgumentException("Item requires quantity");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.PRICE)) {
            Integer price = values.getAsInteger(ItemContract.ItemEntry.PRICE);
            if (price < 0) {
                throw new IllegalArgumentException("Item requires price");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.MAIL)) {
            String mail = values.getAsString(ItemContract.ItemEntry.MAIL);
            if (mail == null) {
                throw new IllegalArgumentException("Item requires mail");
            }

            long newRowId;

            SQLiteDatabase db = itemDbHelper.getWritableDatabase();

            final int match = uriMatcher.match(uri);

            switch (match) {
                case ITEMS:
                    getContext().getContentResolver().notifyChange(uri,null);
                    newRowId = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, newRowId);
            }

        }
        return null;
    }

    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase db = itemDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return rowsDeleted;


            case ITEMS_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ItemContract.ItemEntry.PRODUCT_NAME)) {
            String name = values.getAsString(ItemContract.ItemEntry.PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires name");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.QUANTITY)) {
            Integer quantity = values.getAsInteger(ItemContract.ItemEntry.QUANTITY);
            if (quantity < 0) {
                throw new IllegalArgumentException("Item requires quantity");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.PRICE)) {
            Integer price = values.getAsInteger(ItemContract.ItemEntry.PRICE);
            if (price < 0) {
                throw new IllegalArgumentException("Item requires price");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.MAIL)) {
            String mail = values.getAsString(ItemContract.ItemEntry.MAIL);
            if (mail == null) {
                throw new IllegalArgumentException("Item requires mail");
            }
        }

            int rowsUpdated;
            SQLiteDatabase db = itemDbHelper.getWritableDatabase();
            final int match = uriMatcher.match(uri);
            switch (match) {

                case ITEMS:
                    rowsUpdated = db.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return rowsUpdated;

                case ITEMS_ID:
                    selection = ItemContract.ItemEntry._ID + "=?";
                    selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    rowsUpdated = db.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return rowsUpdated;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }


    }
}
