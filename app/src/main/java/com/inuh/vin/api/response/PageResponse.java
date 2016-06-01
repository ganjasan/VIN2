package com.inuh.vin.api.response;


import com.inuh.vin.models.Page;

import java.util.Collection;

public class PageResponse {

    private int offset;
    private String nextPage;
    private int totalObjects;

    private Collection<Page> data;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }

    public Collection<Page> getData() {
        return data;
    }

    public void setData(Collection<Page> data) {
        this.data = data;
    }
}
