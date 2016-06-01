package com.inuh.vin.models;

import android.content.ContentValues;

/**
 * Created by artimus on 16.05.16.
 */
abstract class Model {

    protected long     _id;
    protected String   objectId;
    protected long     created;
    protected long     updated;


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public abstract ContentValues toContentValues();





}
