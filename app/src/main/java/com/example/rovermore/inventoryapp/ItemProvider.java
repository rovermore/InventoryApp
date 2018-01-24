package com.example.rovermore.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by robertomoreno on 21/1/18.
 */

public class ItemProvider extends ContentProvider {

    private static final int ITEMS = 101;
    private static final int ITEMS_ID = 102;

    ItemDbHelper itemDbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEMS_ID);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

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


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        //This method evaluates if the data in the ContentValues is ok to introduce in the database
        if (!areValuesValidated(values)) {
            throw new IllegalArgumentException("Data not valid");
        } else {

            long newRowId;
            SQLiteDatabase db = itemDbHelper.getWritableDatabase();
            final int match = uriMatcher.match(uri);
            switch (match) {
                case ITEMS:
                    newRowId = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
                    return ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, newRowId);
            }
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase db = itemDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                return rowsDeleted;


            case ITEMS_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                return rowsDeleted;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        //This method evaluates if the data in the ContentValues is ok to introduce in the database
        if (!areValuesValidated(values)) {
            throw new IllegalArgumentException("Data not valid");
        } else {

            int rowsUpdated;
            SQLiteDatabase db = itemDbHelper.getWritableDatabase();
            final int match = uriMatcher.match(uri);
            switch (match) {

                case ITEMS:
                    rowsUpdated = db.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
                    return rowsUpdated;

                case ITEMS_ID:
                    selection = ItemContract.ItemEntry._ID + "=?";
                    selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    rowsUpdated = db.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
                    return rowsUpdated;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
            }

        }
    }

    //This method evaluates if the data in the ContentValues is ok to introduce in the database
    public boolean areValuesValidated(ContentValues values) {

        boolean areValuesValidated = true;

        if (values.containsKey(ItemContract.ItemEntry.PRODUCT_NAME)) {
            String name = values.getAsString(ItemContract.ItemEntry.PRODUCT_NAME);
            if (name == null) {
                areValuesValidated = false;
                return areValuesValidated;
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.QUANTITY)) {
            Integer quantity = values.getAsInteger(ItemContract.ItemEntry.QUANTITY);
            if (quantity < 0 && quantity == null) {
                areValuesValidated = false;
                return areValuesValidated;
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.PRICE)) {
            Integer price = values.getAsInteger(ItemContract.ItemEntry.PRICE);
            if (price < 0 && price == null) {
                areValuesValidated = false;
                return areValuesValidated;
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.MAIL)) {
            String mail = values.getAsString(ItemContract.ItemEntry.MAIL);
            if (mail == null) {
                areValuesValidated = false;
                return areValuesValidated;
            }
        }

        return areValuesValidated;
    }
}
