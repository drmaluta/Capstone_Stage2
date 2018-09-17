package com.maluta.newsnow.interfaces;

import com.maluta.newsnow.models.Article;

import java.util.ArrayList;

/**
 * Created by admin on 9/15/2018.
 */

public interface OnTaskCompleted {
    void onFetchArticles(ArrayList<Article> articles);
}
