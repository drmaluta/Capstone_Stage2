package com.maluta.newsnow.utils;

/**
 * Created by admin on 9/15/2018.
 */

public class ApiUtils {
    public static ArticleClient getArticleClient() {
        return ApiClient.getClient().create(ArticleClient.class);
    }
}
