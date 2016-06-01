package com.inuh.vin.api.requests;

import com.inuh.vin.api.response.NovelResponse;
import com.inuh.vin.api.rest.VinRest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by artimus on 30.05.16.
 */
public class NovelRequest extends RetrofitSpiceRequest<NovelResponse, VinRest> {

    private int mOffset;
    private String mWhereParam;
    private int mPageSize = 50;
    private String mSortClause;

    public NovelRequest(int offset, String whereParam, String sortClause){
        super(NovelResponse.class, VinRest.class);
        mOffset = offset;
        mWhereParam = whereParam;
        mSortClause = sortClause;
    }

    @Override
    public NovelResponse loadDataFromNetwork() throws Exception {
        return getService().getNovels(mOffset, mPageSize, mWhereParam, mSortClause);
    }
}
