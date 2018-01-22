package com.example.rovermore.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open DetailXActivity
        ImageButton add = (ImageButton) findViewById(R.id.ic_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return  true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.dummy_data:
                //CALLS THE INSERT DUMMY DATA METHOD
                return true;
            case R.id.delete_all:
                //calls a method that sets an alert dialog and delete al entries in the table if selected
                return true;
            case R.id.home:
                //Alert dialog if something was edited
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
