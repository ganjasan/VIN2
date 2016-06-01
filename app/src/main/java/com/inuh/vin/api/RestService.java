package com.inuh.vin.api;


import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public class RestService extends RetrofitGsonSpiceService {

    public static final int WEBSERVICE_TIMEOUT = 10000;

    private final static String BASE_URL = "https://api.backendless.com/";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}
