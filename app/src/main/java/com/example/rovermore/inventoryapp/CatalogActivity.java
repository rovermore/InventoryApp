package com.example.rovermore.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public String[] projection = new String[]{
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.PRODUCT_NAME,
            ItemContract.ItemEntry.QUANTITY,
            ItemContract.ItemEntry.PRICE,
            ItemContract.ItemEntry.MAIL
    };

    ItemCursorAdapter itemCursorAdapter;

    ItemDbHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open DetailActivity
        ImageButton add = (ImageButton) findViewById(R.id.ic_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ItemDbHelper(this);


        itemCursorAdapter = new ItemCursorAdapter(this,null);

        ListView itemListView = (ListView) findViewById(R.id.list_view);

        itemListView.setAdapter(itemCursorAdapter);


        itemListView.setEmptyView(findViewById(R.id.empty_view));

        getSupportLoaderManager().initLoader(0,null,this);

        //SI USO ESTE BLOQUE AL RESTO DE VISTAS CLICABLES QUE CONTIENE ESTA VISTA
        /*itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),"test click", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);

                Uri uriCurrentItem = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI,id);

                intent.setData(uriCurrentItem);

                startActivity(intent);
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return  true;
    }


    public void insertDummyData(){

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.PRODUCT_NAME,"Ordenador");
        values.put(ItemContract.ItemEntry.QUANTITY,"3");
        values.put(ItemContract.ItemEntry.PRICE,"1500");
        values.put(ItemContract.ItemEntry.MAIL,"rovermore@gmail.com");

        Uri newItemInserted = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI,values);

        Toast.makeText(getApplicationContext(),"New item inserted with id " + String.valueOf(ContentUris.parseId(newItemInserted)),Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.dummy_data:
                insertDummyData();
                return true;
            case R.id.delete_all:
                //calls a method that sets an alert dialog and delete al entries in the table if selected
                deleteAllAlert();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, ItemContract.ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        itemCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        itemCursorAdapter.swapCursor(null);
    }

    public void deleteAllAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all);
        builder.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //method to delete all data
                deleteData();
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void deleteData(){

        int rowsDeleted = getContentResolver().delete(ItemContract.ItemEntry.CONTENT_URI,null,null);

        Toast.makeText(this,"Rows deleted: " + rowsDeleted,Toast.LENGTH_LONG).show();
    }

}
