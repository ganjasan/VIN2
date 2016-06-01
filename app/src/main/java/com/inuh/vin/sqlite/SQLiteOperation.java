package com.inuh.vin.sqlite;


public interface SQLiteOperation {

    int INSERT = 1;

    int UPDATE = 2;

    int DELETE = 3;

    String KEY_LAST_ID = "com.inuh.vin.sqlite.KEY_LAST_ID";

    String KEY_AFFECTED_ROWS = "com.inuh.vin.sqlite.KEY_AFFECTED_ROWS";
}

