package com.maluta.newsnow.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.maluta.newsnow.database.AppDatabase;
import com.maluta.newsnow.models.Article;

import java.util.List;

/**
 * Created by admin on 9/26/2018.
 */

public class ArticlesAsyncTaskLoader extends AsyncTaskLoader<List<Article>> {
    private List<Article> data = null;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public ArticlesAsyncTaskLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public List<Article> loadInBackground() {
        AppDatabase mDb = AppDatabase.getInstance(mContext);
        List<Article> articles = mDb.articleDao().getAllArticles();
        return articles;
    }

    public void deliverResult(List<Article> data) {
        this.data = data;
        super.deliverResult(data);
    }
}
