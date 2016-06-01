package com.inuh.vin.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by artimus on 01.06.16.
 */
public class SourceProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "Source";

    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" +TABLE_NAME);


    public SourceProvider() {
        super(TABLE_NAME);
    }

    public static long getId(Cursor c){
        return c.getLong(c.getColumnIndex(Columns._ID));
    }

    public static String getObjectID(Cursor c){
        return c.getString(c.getColumnIndex(Columns.OBJECT_ID));
    }

    public static long getCreated(Cursor c){
        return c.getLong(c.getColumnIndex(Columns.CREATED));
    }

    public static String getName(Cursor c){
        return c.getString(c.getColumnIndex(Columns.NAME));
    }

    public static String getDescription(Cursor c){
        return c.getString(c.getColumnIndex(Columns.DESCRIPTION));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    public static Uri getUriWithId(String id){
        return Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME + "/" + id);
    }

    @Override
    public void onContentChanged(Context context, int operation, Bundle extras) {
        super.onContentChanged(context, operation, extras);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns._ID + " integer primary key on conflict replace, "
                + Columns.OBJECT_ID + " text, "
                + Columns.CREATED + " integer, "
                + Columns.UPDATED + " integer, "
                + Columns.NAME + " text, "
                + Columns.LINK + " text, "
                + Columns.DESCRIPTION + " text );");
        db.execSQL("create index if not exists " +
                TABLE_NAME + "_" + Columns.OBJECT_ID + "_index" +
                " on " + TABLE_NAME + "(" + Columns.OBJECT_ID + ");");
    }

    public interface Columns extends BackendColumns {
        String NAME = "name";
        String DESCRIPTION = "description";
        String LINK =" link";
    }
}
