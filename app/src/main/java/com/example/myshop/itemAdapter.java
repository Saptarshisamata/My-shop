package com.example.myshop;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshop.data.itemContract;

public class itemAdapter extends CursorAdapter {

    public itemAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView tName = view.findViewById(R.id.name);
        final TextView tQuantity = view.findViewById(R.id.quantity);
        TextView tPrice = view.findViewById(R.id.price);
        Button saleButton = view.findViewById(R.id.sale);
        final int _id = cursor.getInt(cursor.getColumnIndex(itemContract.itemEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_NAME));
        final int quantity = cursor.getInt(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_PRICE));
        String sQuantity = "Quantity:"+ quantity + " pics";
        String sPrice = "Price:₹" + price;
        tName.setText(name);
        tQuantity.setText(sQuantity );
        tPrice.setText( sPrice);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>0) {
                    int q = quantity - 1;
                    String sQ = "Quantity:" + q + " pics";
                    tQuantity.setText(sQ);
                    Uri uri = ContentUris.withAppendedId(itemContract.itemEntry.CONTENT_URI, _id);

                    ContentValues values = new ContentValues();
                    values.put(itemContract.itemEntry.COLUMN_ITEM_QUANTITY, q);
                    context.getContentResolver().update(uri, values, null, null);
                }else {
                    Toast.makeText(context,"You have no item for sell",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
