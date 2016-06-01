package com.inuh.vin.api.rest;


import com.inuh.vin.api.response.NovelResponse;
import com.inuh.vin.api.response.PageResponse;
import com.inuh.vin.api.response.SourceResponse;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public interface VinRest {

    @Headers({
            "application-id: 7444C9F0-61B7-8E3B-FFF7-5D156E2E3400",
            "secret-key: 2CF64E2E-F2E4-29EE-FF09-62A49BFD8000"
    })
    @GET("/v1/data/Sources")
    SourceResponse getSources(
            @Query("offset") int offset,
            @Query("where") String whereClause);



    @Headers({
            "application-id: 7444C9F0-61B7-8E3B-FFF7-5D156E2E3400",
            "secret-key: 2CF64E2E-F2E4-29EE-FF09-62A49BFD8000"
    })
    @GET("/v1/data/Novels")
    NovelResponse getNovels(
            @Query("offset") int offset,
            @Query("pageSize") int pageSize,
            @Query("where") String whereParam,
            @Query("sortBy") String sortClause
    );



    @Headers({
            "application-id: 7444C9F0-61B7-8E3B-FFF7-5D156E2E3400",
            "secret-key: 2CF64E2E-F2E4-29EE-FF09-62A49BFD8000"
    })
    @GET("/v1/data/Pages")
    PageResponse getPages(
            @Query("offset") int offset,
            @Query("pageSize") int pageSize,
            @Query("where") String whereClause,
            @Query("sortBy") String sortClause
    );





}
