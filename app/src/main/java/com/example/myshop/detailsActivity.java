package com.example.myshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myshop.data.itemContract;

public class detailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0 ;

    EditText nameString;
    EditText priceString;
    EditText quantityString;
    EditText feedbackString;
    Uri currentUri;
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
                insertItem();
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
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:
                return false;
        }
    }

    private void insertItem(){

        String name = nameString.getText().toString().trim();
        String sPrice = priceString.getText().toString().trim();
        String sQuantity = quantityString.getText().toString().trim();
        String feedback = feedbackString.getText().toString().trim();

        int price = Integer.parseInt(sPrice);
        int quantity = Integer.parseInt(sQuantity);

        ContentValues values = new ContentValues();

        values.put(itemContract.itemEntry.COLUMN_ITEM_NAME,name);
        values.put(itemContract.itemEntry.COLUMN_ITEM_PRICE,price);
        values.put(itemContract.itemEntry.COLUMN_ITEM_QUANTITY,quantity);
        values.put(itemContract.itemEntry.COLUMN_USER_REPORT,feedback);

        Uri newUri = getContentResolver().insert(itemContract.itemEntry.CONTENT_URI,values);

        if (newUri == null){
            Toast.makeText(this,"error insert data",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"item inserted",Toast.LENGTH_SHORT).show();
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
