package com.example.rovermore.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by robertomoreno on 20/1/18.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.delete:
                //CREATE DIALOG AND DELETE ENTRY ON THE TABLE
                return true;
            case R.id.save:
                //CALLS INSERT ITEM METHOD (WILL INSERT OR UPDATE DEPENDING ON THE PATH)
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
