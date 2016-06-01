package com.inuh.vin.provider;

import android.net.Uri;

/**
 * Created by artimus on 29.05.16.
 */
public final class TableContracts {

    public final static String AUTHORITY = "com.inuh.vin.provider";
    public final static String SCHEME = "content://";
    public final static String URI_PREFIX = SCHEME + AUTHORITY;


    public final static class TableSource implements BaseTable{

        public final static String TABLE_NAME = "Source";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";

        private TableSource(){

        }

        public static String getCreatingSqlString(){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("CREATE TABLE " + TABLE_NAME + " (");
            sqlBuilder.append(_ID + " INTEGER, ");
            sqlBuilder.append(OBJECT_ID + " TEXT, ");
            sqlBuilder.append(CREATED + " INTEGER, ");
            sqlBuilder.append(UPDATED + " INTEGER, ");
            sqlBuilder.append(NAME + " TEXT,");
            sqlBuilder.append(DESCRIPTION + " TEXT");
            sqlBuilder.append(" );");

            String sql = sqlBuilder.toString();

            return sql;
        }
    }

    public final static class TableNovel implements BaseTable{

        public final static String TABLE_NAME = "Source";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String IMG_HREF = "imgHref";
        public final static String IS_FAVORITE = "isFavorite";
        public final static String IS_DOWNLOADED = "isDownloaded";
        public final static String STATUS_ID = "statusObjectId";
        public final static String RATING = "rating";
        public final static String PAGE_TOTAL = "pageTotal";

        private TableNovel(){

        }

        public static String getCreatingSqlString(){
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("CREATE TABLEPrefManager.getInstance(getContext()).setCurrentDateAsLastUpdate();poc " + TABLE_NAME + " (");
            sqlBuilder.append(_ID + " INTEGER, ");
            sqlBuilder.append(OBJECT_ID + " TEXT, ");
            sqlBuilder.append(CREATED + " INTEGER, ");
            sqlBuilder.append(UPDATED + " INTEGER, ");
            sqlBuilder.append(NAME + " TEXT,");
            sqlBuilder.append(DESCRIPTION + " TEXT, ");
            sqlBuilder.append(IMG_HREF + " TEXT, ");
            sqlBuilder.append(IS_FAVORITE + " INTEGER, ");
            sqlBuilder.append(IS_DOWNLOADED + " INTEGER, ");
            sqlBuilder.append(STATUS_ID + " TEXT, ");
            sqlBuilder.append(RATING + " INTEGER, ");
            sqlBuilder.append(PAGE_TOTAL + " INTEGER ");
            sqlBuilder.append(" );");

            String sql = sqlBuilder.toString();

            return sql;
        }



    }

    public static Uri getContentURI(String tableName){
        return Uri.parse(URI_PREFIX +"/"+tableName);
    }



}
