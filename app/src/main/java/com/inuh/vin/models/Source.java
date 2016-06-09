package com.inuh.vin.models;

import android.app.ActionBar;
import android.content.ContentValues;

import com.inuh.vin.sqlite.SourceProvider.Columns;

import java.io.Serializable;

public class Source extends Model implements Serializable {

    private String name;
    private String description;

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(Columns.OBJECT_ID, objectId);
        cv.put(Columns.UPDATED, updated);
        cv.put(Columns.CREATED, created);
        cv.put(Columns.NAME, name);
        cv.put(Columns.DESCRIPTION, description);

        return cv;

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
}
