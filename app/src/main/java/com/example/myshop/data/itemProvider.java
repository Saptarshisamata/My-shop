package com.example.myshop.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.myshop.data.itemContract.CONTENT_AUTHORITY;
import static com.example.myshop.data.itemContract.PATH_ITEMS;
import static com.example.myshop.data.itemContract.itemEntry;


public class itemProvider extends ContentProvider {

    public static final String LOG_TAG = itemProvider.class.getSimpleName();

    public static final int ITEMS = 1000;
    public static final int ITEM_ID = 1001;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_ITEMS,ITEMS);
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_ITEMS + "/#",ITEM_ID);
    }

    private shopDbHelper dbHelper ;

    @Override
    public boolean onCreate() {

        dbHelper = new shopDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){

            case ITEMS :
                cursor = database.query(itemEntry.TABLE_NAME,projection,null,null,null,null,null);
                break;
            case ITEM_ID:
                selection = itemEntry._ID + "=?" ;
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))} ;
                cursor = database.query(itemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            default:
                throw  new IllegalArgumentException("cannot query unknown uri" + uri) ;

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
