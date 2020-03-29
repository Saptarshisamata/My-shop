package com.example.myshop;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class item_adapter extends ArrayAdapter<item> {
    private int resourceLayout ;
    private Context context;

    public item_adapter(@NonNull Context context, int resource, ArrayList<item> items) {
        super(context, resource,items);
        this.resourceLayout = resource;
        this.context = context ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout,null);
        }
        item p= getItem(position);
        if (p != null) {
            TextView name = v.findViewById(R.id.name);
            TextView quantity = v.findViewById(R.id.quantity);
            TextView price = v.findViewById(R.id.price);
            ImageView sell = v.findViewById(R.id.sell);

            if (name != null){
                name.setText(p.getItemName());
            }
            if (price != null){
                price.setText(p.getPrice());
            }
            if (quantity != null){
                quantity.setText(p.getQuantity());
            }
        }

       return  v;
    }
}
