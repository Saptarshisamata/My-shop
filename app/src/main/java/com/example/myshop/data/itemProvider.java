package com.example.myshop.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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
                cursor = database.query(itemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case ITEM_ID:
                selection = itemEntry._ID + "=?" ;
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))} ;
                cursor = database.query(itemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw  new IllegalArgumentException("cannot query unknown uri" + uri) ;

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int matcher = sUriMatcher.match(uri);

        switch (matcher){
            case ITEM_ID:
                return itemEntry.CONTENT_ITEM_TYPE;
            case ITEMS:
                return itemEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("unknown uri" + uri + "with match" + matcher);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int matcher  = sUriMatcher.match(uri);
        switch (matcher){
            case ITEMS:
                assert values != null;
                return insertItem(uri,values);
            default:
                throw new IllegalArgumentException("insertion is not supported for this "+ uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {


        String name = values.getAsString(itemEntry.COLUMN_ITEM_NAME);

        if (name==null){
            throw new IllegalArgumentException("item needs a name");
        }

        Integer price = values.getAsInteger(itemEntry.COLUMN_ITEM_PRICE);

        if (price != null && price<0){
            throw new IllegalArgumentException("item requires a valid price");
        }

        if (price == null){
            throw new IllegalArgumentException("item must have a price");
        }

        Integer quantity = values.getAsInteger(itemEntry.COLUMN_ITEM_QUANTITY);

        if (quantity != null && quantity<0){
            throw new IllegalArgumentException("item must have a valid quantity");
        }


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(itemEntry.TABLE_NAME,null,values);

        if(id==-1){
            Toast.makeText(getContext(),"insert failed for row"+uri,Toast.LENGTH_SHORT).show();
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int matcher = sUriMatcher.match(uri);
        int rowDeleted;
        switch (matcher){
            case ITEMS:
                rowDeleted =  database.delete(itemEntry.TABLE_NAME,selection,selectionArgs);
                if (rowDeleted != 0){
                    Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);
                }
                return rowDeleted;
            case ITEM_ID:

                selection = itemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted =  database.delete(itemEntry.TABLE_NAME,selection,selectionArgs);
                if (rowDeleted != 0){
                    Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);
                }
                return rowDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
       int matcher = sUriMatcher.match(uri);

       switch (matcher){
           case ITEMS:
               assert values != null;
               return updateItem(uri,values,selection,selectionArgs);
           case ITEM_ID:

               selection = itemEntry._ID + "=?";
               selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
               assert values != null;
               return updateItem(uri, values, selection, selectionArgs);
           default:
               throw new IllegalArgumentException("update is not supported for" + uri);
       }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(itemEntry.COLUMN_ITEM_NAME)){
            String name = values.getAsString(itemEntry.COLUMN_ITEM_NAME);
                if (name == null){
                    throw new IllegalArgumentException("item must have a name");
                }
        }

        if (values.containsKey(itemEntry.COLUMN_ITEM_QUANTITY)){
            Integer quantity = values.getAsInteger(itemEntry.COLUMN_ITEM_QUANTITY);
            if (quantity != null && quantity<0) {
                throw new IllegalArgumentException("quantity must be positive");
            }
        }

        if (values.containsKey(itemEntry.COLUMN_ITEM_PRICE)){
            Integer price = values.getAsInteger(itemEntry.COLUMN_ITEM_PRICE);
            if (price == null){
                throw new IllegalArgumentException("item must have a valid price");
            }
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowUpdated =  database.update(itemEntry.TABLE_NAME,values,selection,selectionArgs);

        if (rowUpdated != 0){
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri,null);
        }
        return  rowUpdated;
    }
}
