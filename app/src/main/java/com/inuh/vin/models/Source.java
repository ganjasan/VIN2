package com.inuh.vin.models;

import android.app.ActionBar;
import android.content.ContentValues;

import com.inuh.vin.provider.TableContracts.TableSource;

import java.io.Serializable;

public class Source extends Model implements Serializable {

    private String name;
    private String description;

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(TableSource.OBJECT_ID, objectId);
        cv.put(TableSource.UPDATED, updated);
        cv.put(TableSource.CREATED, created);
        cv.put(TableSource.NAME, name);
        cv.put(TableSource.DESCRIPTION, description);

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
