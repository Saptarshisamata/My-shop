package com.example.myshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myshop.data.itemContract;

public class detailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0 ;

    private boolean itemChanged = false ;

    EditText nameString;
    EditText priceString;
    EditText quantityString;
    EditText feedbackString;
    Uri currentUri;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            itemChanged = true ;
            return false;
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentUri = intent.getData();

        if (currentUri == null){
            setTitle("Add a Item");
        }else {
            setTitle("Edit Item");
            getLoaderManager().initLoader(ITEM_LOADER,null,this);
        }

        nameString = findViewById(R.id.name);
        priceString = findViewById(R.id.price);
        quantityString = findViewById(R.id.quantity);
        feedbackString = findViewById(R.id.feedback);

        nameString.setOnTouchListener(listener);
        priceString.setOnTouchListener(listener);
        quantityString.setOnTouchListener(listener);
        feedbackString.setOnTouchListener(listener);


    }



    private void showUnsavedChangedDialog(DialogInterface.OnClickListener discardButtonListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing");
        builder.setPositiveButton("DISCARD",discardButtonListener);
        builder.setNegativeButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!itemChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangedDialog(discardButtonListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.detailmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id){
            case R.id.done:
                saveItem();
                finish();
                return true;
            case R.id.dlt_item:
                int rowsDeleted = getContentResolver().delete(currentUri,null,null);
                if (rowsDeleted == 0){
                    Toast.makeText(this,"error while delete",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"deleted",Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            case android.R.id.home:
                if (!itemChanged){
                    //Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                //Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
                DialogInterface.OnClickListener discardButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(detailsActivity.this);
                    }
                };
                showUnsavedChangedDialog(discardButtonListener);
                return true;

            default:
                return false;
        }
    }

    private void saveItem(){

        String name = nameString.getText().toString().trim();
        String sPrice = priceString.getText().toString().trim();
        String sQuantity = quantityString.getText().toString().trim();
        String feedback = feedbackString.getText().toString().trim();

        if (currentUri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(sPrice) && TextUtils.isEmpty(sQuantity)){
            return;
        }
        if (TextUtils.isEmpty(feedback)){
            feedback = "" ;
        }

        int price = Integer.parseInt(sPrice);
        int quantity = Integer.parseInt(sQuantity);

        ContentValues values = new ContentValues();

        values.put(itemContract.itemEntry.COLUMN_ITEM_NAME,name);
        values.put(itemContract.itemEntry.COLUMN_ITEM_PRICE,price);
        values.put(itemContract.itemEntry.COLUMN_ITEM_QUANTITY,quantity);
        values.put(itemContract.itemEntry.COLUMN_USER_REPORT,feedback);
        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(itemContract.itemEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "error insert data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "item inserted", Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowEffected = getContentResolver().update(currentUri,values,null,null);

            if (rowEffected == 0){
                Toast.makeText(this,"Item updated successfully",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"error happened during upload",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                itemContract.itemEntry._ID,
                itemContract.itemEntry.COLUMN_ITEM_NAME,
                itemContract.itemEntry.COLUMN_ITEM_PRICE,
                itemContract.itemEntry.COLUMN_ITEM_QUANTITY,
                itemContract.itemEntry.COLUMN_USER_REPORT
        };

        return new CursorLoader(this,currentUri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_NAME));
            int sPrice = data.getInt(data.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_PRICE));
            int sQuantity = data.getInt(data.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_QUANTITY));
            String feedback = data.getString(data.getColumnIndex(itemContract.itemEntry.COLUMN_USER_REPORT));

            String price = String.valueOf(sPrice);
            String quantity = String.valueOf(sQuantity);

            nameString.setText(name);
            priceString.setText(price);
            quantityString.setText(quantity);
            feedbackString.setText(feedback);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameString.setText("");
        priceString.setText("");
        quantityString.setText("");
        feedbackString.setText("");
    }
}
