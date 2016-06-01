package com.inuh.vin.api.response;


import com.inuh.vin.models.Novel;

import java.util.Collection;

public class NovelResponse {

    private int offset;
    private String nextPage;
    private int totalObjects;

    private Collection<Novel> data;

    public NovelResponse(){

    }

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

    public Collection<Novel> getData() {
        return data;
    }

    public void setData(Collection<Novel> data) {
        this.data = data;
    }
}
