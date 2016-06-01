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
public class NovelProvider extends SQLiteTableProvider {

    public static final String TABLE_NAME = "novel";

    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" +TABLE_NAME);

    public NovelProvider() {
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

    public static String getImgHref(Cursor c){
        return c.getString(c.getColumnIndex(Columns.IMG_HREF));
    }

    public static String getHref(Cursor c){
        return c.getString(c.getColumnIndex(Columns.HREF));
    }

    public static int getPageTotal(Cursor c){
        return c.getInt(c.getColumnIndex(Columns.PAGE_TOTAL));
    }

    public static int getRating(Cursor c){
        return c.getInt(c.getColumnIndex(Columns.RATING));
    }

    public static String getStatus(Cursor c){
        return c.getString(c.getColumnIndex(Columns.STATUS));
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists " + TABLE_NAME +
                "(" + Columns._ID + " integer primary key on conflict replace, "
                + Columns.OBJECT_ID + " text, "
                + Columns.CREATED + " integer, "
                + Columns.UPDATED + " integer, "
                + Columns.NAME + " text, "
                + Columns.DESCRIPTION + " text, "
                + Columns.IMG_HREF + " text, "
                + Columns.HREF + " text, "
                + Columns.PAGE_TOTAL + " integer, "
                + Columns.RATING + " integer, "
                + Columns.STATUS + " text );");
        db.execSQL("create index if not exists " +
            TABLE_NAME + "_" + Columns.OBJECT_ID + "_index" +
            " on " + TABLE_NAME + "(" + Columns.OBJECT_ID + ");");
    }

    public interface Columns extends BackendColumns {
        String NAME = "name";
        String DESCRIPTION = "description";
        String IMG_HREF ="imgHref";
        String HREF = "href";
        String PAGE_TOTAL = "pageTotal";
        String RATING = "rating";
        String STATUS = "status";
    }
}
