package com.inuh.vin.provider;

import android.app.ActionBar;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.inuh.vin.provider.TableContracts.*;

import java.sql.SQLException;

public class DataProvider extends ContentProvider {

    private static final int SOURCE = 1;
    private static final int SOURCE_ID = 2;
    private static final int NOVEL = 3;
    private static final int NOVEL_ID = 4;


    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.content";
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.item";

    private DBHelper mDBHelper;
    private UriMatcher mUriMatcher;


    @Override
    public boolean onCreate() {

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableSource.TABLE_NAME, SOURCE);
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableSource.TABLE_NAME + "/#", SOURCE_ID);
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableNovel.TABLE_NAME, NOVEL);
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableNovel.TABLE_NAME +"/#", NOVEL_ID);

        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String id;

        switch (mUriMatcher.match(uri)){
            case SOURCE:
                queryBuilder.setTables(TableSource.TABLE_NAME);
                break;

            case SOURCE_ID:
                queryBuilder.setTables(TableSource.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(TableSource.OBJECT_ID + "=" + id);
                break;

            case NOVEL:
                queryBuilder.setTables(TableNovel.TABLE_NAME);
                break;

            case NOVEL_ID:
                queryBuilder.setTables(TableNovel.TABLE_NAME);
                id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(TableSource.OBJECT_ID + "=" + id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        int updateRowsCount;
        String id;

        db.beginTransaction();
        try{
            switch (mUriMatcher.match(uri)){
                case SOURCE:
                    updateRowsCount = db.update(TableSource.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case SOURCE_ID:
                    id = uri.getPathSegments().get(1);
                    selection = appendRowId(TableSource.TABLE_NAME, id);
                    updateRowsCount = db.update(TableSource.TABLE_NAME, values,selection,selectionArgs);

                    break;

                case NOVEL:
                    updateRowsCount = db.update(TableNovel.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case NOVEL_ID:
                    id = uri.getPathSegments().get(1);
                    selection = appendRowId(TableNovel.TABLE_NAME, id);
                    updateRowsCount = db.update(TableNovel.TABLE_NAME, values,selection,selectionArgs);

                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);

            }

        }finally {
            db.endTransaction();
        }

        if(updateRowsCount > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateRowsCount;

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values){

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (values == null){
            throw new IllegalArgumentException("Content values arg for insert() is null");
        }

        switch (mUriMatcher.match(uri)) {
            case NOVEL:
                db.insert(TableNovel.TABLE_NAME,null, values);
                getContext().getContentResolver().notifyChange(TableNovel.CONTENT_URI,null);
                break;

            case SOURCE:
                db.insert(TableSource.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(TableSource.CONTENT_URI, null);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return uri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int deleteRowsCount;
        String id;

        db.beginTransaction();

        try{
            switch (mUriMatcher.match(uri)){
                case NOVEL:
                    deleteRowsCount = db.delete(TableNovel.TABLE_NAME, selection, selectionArgs);
                    break;

                case NOVEL_ID:
                    id = uri.getPathSegments().get(1);
                    selection = appendRowId(selection, id);
                    deleteRowsCount = db.delete(TableNovel.TABLE_NAME, selection, selectionArgs);
                    break;

                case SOURCE:
                    deleteRowsCount = db.delete(TableSource.TABLE_NAME, selection, selectionArgs);
                    break;

                case SOURCE_ID:
                    id = uri.getPathSegments().get(1);
                    selection = appendRowId(selection, id);
                    deleteRowsCount = db.delete(TableSource.TABLE_NAME, selection, selectionArgs);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }finally {
            db.endTransaction();
        }

        if (deleteRowsCount>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteRowsCount;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = mUriMatcher.match(uri);

        if(match == NOVEL || match == SOURCE) {
            return CONTENT_TYPE;
        }else if(match == NOVEL_ID || match == SOURCE_ID){
            return CONTENT_ITEM_TYPE;
        }else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private String appendRowId(String selection, String id) {
        return BaseTable.OBJECT_ID
                + "="
                + id
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : "");
    }


}
