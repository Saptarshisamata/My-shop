package com.example.myshop;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshop.data.itemContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0 ;
    itemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView itemList =(ListView) findViewById(R.id.list);
        View view = findViewById(R.id.emptyView);
        itemList.setEmptyView(view);
        mItemAdapter = new itemAdapter(this,null,0);

        itemList.setAdapter(mItemAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,detailsActivity.class);
                Uri currentUri = ContentUris.withAppendedId(itemContract.itemEntry.CONTENT_URI,id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
        FloatingActionButton addButton = findViewById(R.id.addItem);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,detailsActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(ITEM_LOADER,null,this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.homemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId() ;

        switch (id){
            case R.id.dlt :
                getContentResolver().delete(itemContract.itemEntry.CONTENT_URI,null,null);
                return true ;
            default:
                throw new IndexOutOfBoundsException("not valid item selected");
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                itemContract.itemEntry._ID,
                itemContract.itemEntry.COLUMN_ITEM_NAME,
                itemContract.itemEntry.COLUMN_ITEM_PRICE,
                itemContract.itemEntry.COLUMN_ITEM_QUANTITY
        };

        return new CursorLoader(this,itemContract.itemEntry.CONTENT_URI,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mItemAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemAdapter.swapCursor(null);
    }
}
