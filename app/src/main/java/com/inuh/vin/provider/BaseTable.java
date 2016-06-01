package com.inuh.vin.provider;

import android.provider.BaseColumns;

/**
 * Created by artimus on 29.05.16.
 */
public interface BaseTable extends BaseColumns {

    /* object UUID from backendles
    TYPE: STRING(36)
     */
    public static final String OBJECT_ID = "objectID";
    /* last update date
    TYPE: INTEGER
     */
    public static final String UPDATED = "updated";
    /* date of creating
    TYPE: INTEGER
     */
    public static final String CREATED = "created";


}
