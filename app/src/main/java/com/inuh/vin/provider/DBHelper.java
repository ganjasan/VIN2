package com.inuh.vin.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;


public class DBHelper extends SQLiteOpenHelper {

    public final static String TAG = "provider.DBHelper";

    private static final String DATABASE_NAME = "db_vin.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE SOURCE TABLE
        String sql = TableContracts.TableSource.getCreatingSqlString();
        Log.i(TAG, "Creating DB table with string: " + sql);
        db.execSQL(sql);

        //CREATE NOVEL TABLE
        sql = TableContracts.TableNovel.getCreatingSqlString();
        Log.i(TAG, "Creating DB table with string: " + sql);
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
