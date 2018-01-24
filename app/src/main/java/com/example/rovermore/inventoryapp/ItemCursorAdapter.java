package com.example.rovermore.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by robertomoreno on 24/1/18.
 */

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //set views for name quantity and price
        TextView name = (TextView) view.findViewById(R.id.product_name);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price);
        //get values from cursor an save in variables
        String mName = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.PRODUCT_NAME));
        String mQuantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.QUANTITY)));
        String mPrice = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.PRICE)));

        //set values to the views
        name.setText(mName);
        quantity.setText(mQuantity);
        price.setText(mPrice);
    }
}
