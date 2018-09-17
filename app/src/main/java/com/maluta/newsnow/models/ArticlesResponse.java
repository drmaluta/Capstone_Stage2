package com.maluta.newsnow.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 9/12/2018.
 */

public class ArticlesResponse {
    @SerializedName("articles")
    private ArrayList<Article> articles;

    public ArrayList<Article> getResults() {
        return articles;
    }

    public void setResults(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
