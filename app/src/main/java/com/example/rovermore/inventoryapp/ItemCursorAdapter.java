package com.example.rovermore.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by robertomoreno on 24/1/18.
 */

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {

        View row = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        Button saleButton = (Button)row.findViewById(R.id.sale_button);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sellProduct(cursor, context);

            }
        });

        return row;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

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

        int position = cursor.getPosition();
        final long id = getItemId(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"test click", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, DetailActivity.class);

                Uri uriCurrentItem = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI,id);

                intent.setData(uriCurrentItem);

                context.startActivity(intent);
            }
        });
    }

    public void sellProduct(Cursor cursor, Context context){

       int mQuantity = cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.QUANTITY);

       if(mQuantity>0) {
           mQuantity = mQuantity - 1;
       }else {

           Toast.makeText(context, "There's no stock of the product", Toast.LENGTH_LONG).show();
       }

        int position = cursor.getPosition();
        final long id = getItemId(position);

        Uri uriCurrentItem = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI,id);

        //get values from cursor an save in variables
        String mName = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.PRODUCT_NAME));
        String mPrice = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.PRICE)));
        String mMail = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.MAIL)));

        //set values from cursor into the content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.PRODUCT_NAME,mName);
        contentValues.put(ItemContract.ItemEntry.PRICE,mPrice);
        contentValues.put(ItemContract.ItemEntry.QUANTITY, mQuantity);
        contentValues.put(ItemContract.ItemEntry.MAIL, mMail);

        int rowsUpdated = context.getContentResolver().update(uriCurrentItem,contentValues,null,null);

        Toast.makeText(context,"Rows updated: " + rowsUpdated, Toast.LENGTH_LONG);

    }

}
