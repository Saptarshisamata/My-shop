package com.example.myshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myshop.data.itemContract;

public class detailsActivity extends AppCompatActivity {

    EditText nameString;
    EditText priceString;
    EditText quantityString;
    EditText feedbackString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri == null){
            setTitle("Add a Item");
        }else {
            setTitle("Edit Item");
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
                Intent intent = new Intent(detailsActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.dlt_item:
                // delete item
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
            Toast.makeText(this,"item inseted",Toast.LENGTH_SHORT).show();
        }

    }
}
