package com.inuh.vin.models;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by artimus on 16.05.16.
 */
public class Page extends Model implements Serializable{

    private String name;
    private String imgHref;
    private int number;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }
}
