package com.example.rovermore.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.makeText;

/**
 * Created by robertomoreno on 20/1/18.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uri;



    EditText mName;

    EditText mQuantity;

    EditText mPrice;

    EditText mEmail;

    public String[] projection = new String[]{
            ItemContract.ItemEntry._ID,
            ItemContract.ItemEntry.PRODUCT_NAME,
            ItemContract.ItemEntry.QUANTITY,
            ItemContract.ItemEntry.PRICE,
            ItemContract.ItemEntry.MAIL
    };

    //this boolean detects shows if a change was made in the activity by the user
    private boolean mItemHasChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // OnTouchListener that listens for any user touches on a View, implying that they are modifying
        // the view, and we change the mItemHasChanged boolean to true.
        View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mItemHasChanged = true;
                return false;
            }
        };

        setViews();
        mName.setOnTouchListener(mTouchListener);
        mQuantity.setOnTouchListener(mTouchListener);
        mPrice.setOnTouchListener(mTouchListener);
        mEmail.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();

        uri = intent.getData();

        if(uri==null){
            setTitle("New Item");
        }else{
            setTitle("Edit Item");

            getSupportLoaderManager().initLoader(0, null, this);

            Button moreButton = (Button) findViewById(R.id.more_button);
            Button lessButton = (Button) findViewById(R.id.less_button);
            moreButton.setOnTouchListener(mTouchListener);
            lessButton.setOnTouchListener(mTouchListener);
            setOnClickListenerButton(moreButton);
            setOnClickListenerButton(lessButton);
        }

    }

    public void setOnClickListenerButton (Button button){

        final String sign = String.valueOf(button.getText());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textCurrentQuantity = String.valueOf(mQuantity.getText());

                int currentQuantity = Integer.parseInt(textCurrentQuantity);

                if(sign.equalsIgnoreCase("+")){

                    String textQuantity = String.valueOf(mQuantity.getText());

                   int newQuantity = Integer.parseInt(textQuantity);

                   newQuantity = newQuantity +1;

                   mQuantity.setText(String.valueOf(newQuantity));

                }else if(sign.equalsIgnoreCase("-") && currentQuantity > 0) {

                    String textQuantity = String.valueOf(mQuantity.getText());

                    int newQuantity = Integer.parseInt(textQuantity);

                    newQuantity = newQuantity - 1;

                    mQuantity.setText(String.valueOf(newQuantity));
                }else{

                    Toast.makeText(getApplicationContext(),"Negative quantities are not allowed", Toast.LENGTH_LONG);
                }


            }
        });
    }

    public void setViews() {
        mName = (EditText) findViewById(R.id.product_name_detail);
        mQuantity = (EditText) findViewById(R.id.quantity_detail);
        mPrice = (EditText) findViewById(R.id.price_detail);
        mEmail = (EditText) findViewById(R.id.email_detail);


    }


    public void insertItem() {

        String name = mName.getText().toString().trim();
        String quantityText = mQuantity.getText().toString().trim();
        String priceText = mPrice.getText().toString().trim();
        String email = mEmail.getText().toString().trim();

        int quantity = 0;
        int price = 0;
        if (!TextUtils.isEmpty(quantityText)) {
            quantity = Integer.parseInt(quantityText);
        }
        if (!TextUtils.isEmpty(priceText)) {
            price = Integer.parseInt(priceText);
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.PRODUCT_NAME, name);
        contentValues.put(ItemContract.ItemEntry.QUANTITY, quantity);
        contentValues.put(ItemContract.ItemEntry.PRICE, price);
        contentValues.put(ItemContract.ItemEntry.MAIL, email);

        if(uri==null){

            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI,contentValues);

                if (newUri == null) {

                    Toast toast = makeText(this, "Error inserting Item", Toast.LENGTH_LONG);
                    toast.show();

                } else {

                    Toast toast2 = makeText(this, "Succesfully inserted: " + String.valueOf(ContentUris.parseId(newUri)), Toast.LENGTH_LONG);
                    toast2.show();

                }

        }else{

            int rowsUpdated = getContentResolver().update(uri,contentValues,null,null);

            if (rowsUpdated == 0) {

                Toast toast = makeText(this, "Error updating Item", Toast.LENGTH_LONG);
                toast.show();

            } else {

                Toast toast2 = makeText(this, "Succesfully updated: " + String.valueOf(rowsUpdated), Toast.LENGTH_LONG);
                toast2.show();

            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete:
                //CREATE DIALOG AND DELETE ENTRY ON THE TABLE
                if (uri != null) {
                    deleteItemDialog();
                }
                return true;
            case R.id.save:
                //CALLS INSERT ITEM METHOD (WILL INSERT OR UPDATE DEPENDING ON THE PATH)
                if (areValuesValidated()){
                insertItem();
                finish();
                }else{
                    //Toast.makeText(getApplicationContext(),"Please make sure to fill all the mandatory data",Toast.LENGTH_LONG);
                    unCompletedInfo();
                }
                return true;
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                unsavedChanges(discardButtonClickListener);
                return true;
        }

                return super.onOptionsItemSelected(item);

    }

    private void unCompletedInfo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please make sure all fields are filled");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog unCompletedInfo = builder.create();
        unCompletedInfo.show();
    }

    private boolean areValuesValidated() {
        String name = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String quantityText = mQuantity.getText().toString().trim();
        String priceText = mPrice.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            return false;
        }

        if(TextUtils.isEmpty(email)){

            return false;
        }

        if(TextUtils.isEmpty(quantityText)){

            return false;
        }

        if(TextUtils.isEmpty(priceText)){

            return false;
        }

        return true;
    }

    public void deleteItem(Uri uri) {

        int rowsDeleted = getContentResolver().delete(uri, null, null);

        Toast.makeText(this, "Rows deleted: " + rowsDeleted, Toast.LENGTH_LONG).show();

        //toast a different message depending on the number of rows deleted
        if (rowsDeleted != 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Pet deleted", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No pet was deleted", Toast.LENGTH_LONG);
            toast.show();
        }
        finish();
    }

    public void deleteItemDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Delete this item?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(uri);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        if(!mItemHasChanged){
        super.onBackPressed();
        return;}

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        unsavedChanges(discardButtonClickListener);
    }

    public void unsavedChanges(DialogInterface.OnClickListener discardButtonClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton("Quit", discardButtonClickListener);

        AlertDialog unsavedChangesDialog = builder.create();
        unsavedChangesDialog.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(getBaseContext(),uri,projection,null, null, null);


        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst() == false){
            //el cursor está vacío
            return;
        }

        String name = data.getString(data.getColumnIndexOrThrow(ItemContract.ItemEntry.PRODUCT_NAME));

        int quantity = data.getInt(data.getColumnIndexOrThrow(ItemContract.ItemEntry.QUANTITY));

        int price = data.getInt(data.getColumnIndexOrThrow(ItemContract.ItemEntry.PRICE));

        String email = data.getString(data.getColumnIndexOrThrow(ItemContract.ItemEntry.MAIL));

        mName.setText(name);

        mQuantity.setText(Integer.toString(quantity));

        mPrice.setText(Integer.toString(price));

        mEmail.setText(email);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mName.setText(null);

        mQuantity.setText(null);

        mPrice.setText(null);

        mEmail.setText(null);

    }
}
