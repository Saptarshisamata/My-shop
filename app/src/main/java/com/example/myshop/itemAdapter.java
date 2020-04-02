package com.example.myshop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tName = view.findViewById(R.id.name);
        TextView tQuantity = view.findViewById(R.id.quantity);
        TextView tPrice = view.findViewById(R.id.price);

        String name = cursor.getString(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_NAME));
        int quantity = cursor.getInt(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_QUANTITY));
        int price = cursor.getInt(cursor.getColumnIndex(itemContract.itemEntry.COLUMN_ITEM_PRICE));
        String sQuantity = "Quantity:"+ quantity + " pics";
        String sPrice = "Price:₹" + price;
        tName.setText(name);
        tQuantity.setText(sQuantity );
        tPrice.setText( sPrice);
    }
}
