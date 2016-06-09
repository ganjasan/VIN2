package com.inuh.vin.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.inuh.vin.sqlite.NovelProvider.Columns;

import com.inuh.vin.sqlite.SourceProvider;

import java.io.Serializable;


public class Novel extends Model implements Serializable {

    private String  name;
    private String  description;
    private int     rating;
    private String  status;
    private String  imgHref;
    private boolean isFavorite;
    private boolean isDownloaded;
    private int     pageTotal;
    private String  sourceId;


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Columns.OBJECT_ID, objectId);
        cv.put(Columns.CREATED, created);
        cv.put(Columns.UPDATED, updated);
        cv.put(Columns.NAME, name);
        cv.put(Columns.DESCRIPTION, description);
        cv.put(Columns.RATING, rating);
        cv.put(Columns.STATUS, status);
        cv.put(Columns.IMG_HREF, imgHref);
        cv.put(Columns.IS_FAVORITE, isFavorite==true ? 1 : 0);
        cv.put(Columns.IS_DOWNLOADED, isDownloaded == true ? 1 : 0);
        cv.put(Columns.SOURCE_ID, sourceId);
        cv.put(Columns.PAGE_TOTAL, pageTotal);
        return cv;
    }

    public static Novel fromCursor(Cursor cursor){
        Novel newNovel = new Novel();

        newNovel.set_id(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
        newNovel.setObjectId(cursor.getString(cursor.getColumnIndex(Columns.OBJECT_ID)));
        newNovel.setUpdated(cursor.getLong(cursor.getColumnIndex(Columns.UPDATED)));
        newNovel.setCreated(cursor.getLong(cursor.getColumnIndex(Columns.CREATED)));
        newNovel.setName(cursor.getString(cursor.getColumnIndex(Columns.NAME)));
        newNovel.setDescription(cursor.getString(cursor.getColumnIndex(Columns.DESCRIPTION)));
        newNovel.setRating(cursor.getInt(cursor.getColumnIndex(Columns.RATING)));
        newNovel.setStatus(cursor.getString(cursor.getColumnIndex(Columns.STATUS)));
        newNovel.setImgHref(cursor.getString(cursor.getColumnIndex(Columns.IMG_HREF)));
        newNovel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(Columns.IS_FAVORITE)) == 0
                ? false : true);
        newNovel.setIsDownloaded(cursor.getInt(cursor.getColumnIndex(Columns.IS_DOWNLOADED)) == 0
                ? false : true);
        newNovel.setSourceId(cursor.getString(cursor.getColumnIndex(Columns.SOURCE_ID)));
        newNovel.setPageTotal(cursor.getInt(cursor.getColumnIndex(Columns.PAGE_TOTAL)));

        return newNovel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
