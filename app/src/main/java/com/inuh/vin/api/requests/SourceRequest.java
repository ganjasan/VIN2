package com.inuh.vin.api.requests;


import android.util.Log;

import com.inuh.vin.api.response.SourceResponse;
import com.inuh.vin.api.rest.VinRest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class SourceRequest extends RetrofitSpiceRequest<SourceResponse, VinRest> {

    private int offset;
    private int pageSize = 10;
    private String whereClause;

    public SourceRequest(int offset, String whereClause){
        super(SourceResponse.class, VinRest.class);
        this.offset = offset;
        this.whereClause = whereClause;
    }

    @Override
    public SourceResponse loadDataFromNetwork() throws Exception {
        Log.d("RestRequset", "Call web service");
        return getService().getSources(offset, whereClause);
    }
}
