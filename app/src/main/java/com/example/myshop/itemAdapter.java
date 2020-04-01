package com.example.myshop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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

        String name = cursor.getString(cursor.getColumnIndexOrThrow("COLUMN_ITEM_NAME"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("COLUMN_ITEM_QUANTITY"));
        int price = cursor.getInt(cursor.getColumnIndexOrThrow("COLUMN_ITEM_PRICE"));

        tName.setText(name);
        tQuantity.setText(String.valueOf(quantity));
        tPrice.setText(String.valueOf(price));
    }
}
