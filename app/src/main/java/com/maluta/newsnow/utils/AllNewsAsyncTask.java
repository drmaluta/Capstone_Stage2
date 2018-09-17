package com.maluta.newsnow.utils;

import android.os.AsyncTask;

import com.maluta.newsnow.BuildConfig;
import com.maluta.newsnow.interfaces.OnTaskCompleted;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.models.ArticlesResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by admin on 9/16/2018.
 */

public class AllNewsAsyncTask extends AsyncTask<String, Void, ArrayList<Article>> {
    private OnTaskCompleted onTaskCompleted;
    private int mPage;
    public static String my_api_key = BuildConfig.my_api;

    public AllNewsAsyncTask(OnTaskCompleted taskCompleted, int page){
        super();
        onTaskCompleted = taskCompleted;
        mPage = page;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        Map<String, String> options = new HashMap<>();
        options.put("sources", "all");
        options.put("language", "en");
        options.put("page", String.valueOf(mPage));
        options.put("apiKey", my_api_key);

        ArticleClient client = ApiUtils.getArticleClient();
        Call<ArticlesResponse> call = client.getAllNews(options);
        try {
            Response<ArticlesResponse> rp = call.execute();
            return rp.body().getResults();
        } catch (IOException e) {
            Timber.e("error in getting response from service using retrofit", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> result) {
        super.onPostExecute(result);

        if (result != null) {
            onTaskCompleted.onFetchArticles(result);
        }
    }
}
