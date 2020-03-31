package com.example.myshop.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class itemContract {
    private itemContract(){}

    public static final class itemEntry{

        /** name of the database table of items **/

        public static final String TABLE_NAME = "items" ;

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_ITEM_NAME = "name" ;

        public static final String COLUMN_ITEM_QUANTITY = "quantity";

        public static final String COLUMN_ITEM_PRICE = "price";

        public static final String COLUMN_USER_REPORT = "report";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS);
    }
    public static final String CONTENT_AUTHORITY = "com.example.myshop";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY) ;

    public static final String PATH_ITEMS = "items";

}
