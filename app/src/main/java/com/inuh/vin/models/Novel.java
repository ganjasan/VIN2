package com.inuh.vin.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.TabLayout;

import com.inuh.vin.provider.TableContracts.TableNovel;

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


    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(TableNovel.OBJECT_ID, objectId);
        cv.put(TableNovel.CREATED, created);
        cv.put(TableNovel.UPDATED, updated);
        cv.put(TableNovel.NAME, name);
        cv.put(TableNovel.DESCRIPTION, description);
        cv.put(TableNovel.RATING, rating);
        cv.put(TableNovel.STATUS_ID, status);
        cv.put(TableNovel.IMG_HREF, imgHref);
        cv.put(TableNovel.IS_FAVORITE, isFavorite);
        cv.put(TableNovel.IS_DOWNLOADED, isDownloaded);
        return cv;
    }

    public static Novel fromCursor(Cursor cursor){
        Novel newNovel = new Novel();

        newNovel.set_id(cursor.getLong(cursor.getColumnIndex(TableNovel._ID)));
        newNovel.setObjectId(cursor.getString(cursor.getColumnIndex(TableNovel.OBJECT_ID)));
        newNovel.setUpdated(cursor.getLong(cursor.getColumnIndex(TableNovel.UPDATED)));
        newNovel.setCreated(cursor.getLong(cursor.getColumnIndex(TableNovel.CREATED)));
        newNovel.setName(cursor.getString(cursor.getColumnIndex(TableNovel.NAME)));
        newNovel.setDescription(cursor.getString(cursor.getColumnIndex(TableNovel.DESCRIPTION)));
        newNovel.setRating(cursor.getInt(cursor.getColumnIndex(TableNovel.RATING)));
        newNovel.setStatus(cursor.getString(cursor.getColumnIndex(TableNovel.STATUS_ID)));
        newNovel.setImgHref(cursor.getString(cursor.getColumnIndex(TableNovel.IMG_HREF)));
        newNovel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(TableNovel.IS_FAVORITE)) == 0
                ? false : true);
        newNovel.setIsDownloaded(cursor.getInt(cursor.getColumnIndex(TableNovel.IS_DOWNLOADED))== 0
                ? false : true );

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
}
