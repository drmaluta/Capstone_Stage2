package com.maluta.newsnow.utils;

import com.maluta.newsnow.models.ArticlesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by admin on 9/12/2018.
 */

public interface ArticleClient {

    @GET("top-headlines")
    Call<ArticlesResponse> getTopNews(@QueryMap Map<String, String> options, @Query("page") String page, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<ArticlesResponse> getAllNews(@QueryMap Map<String, String> options, @Query("page") String page, @Query("apiKey") String apiKey);
}
