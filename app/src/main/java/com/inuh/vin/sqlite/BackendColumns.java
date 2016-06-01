package com.inuh.vin.sqlite;

import android.provider.BaseColumns;

/**
 * Created by artimus on 01.06.16.
 */
public interface BackendColumns extends BaseColumns {

    /* object UUID from backendles
    <P>TYPE: STRING(36)</P>
     */
    public static final String OBJECT_ID = "objectID";
    /* last update date
    <P>TYPE: INTEGER(long)</P>
     */
    public static final String UPDATED = "updated";
    /* date of creating
    <P>TYPE: INTEGER</P>
     */
    public static final String CREATED = "created";

}
