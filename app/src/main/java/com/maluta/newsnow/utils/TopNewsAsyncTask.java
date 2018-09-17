package com.maluta.newsnow.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.maluta.newsnow.BuildConfig;
import com.maluta.newsnow.adapters.ArticleListAdapter;
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
 * Created by admin on 9/15/2018.
 */

public class TopNewsAsyncTask extends AsyncTask<String, Void, ArrayList<Article>> {
    private OnTaskCompleted onTaskCompleted;

    public static String my_api_key = BuildConfig.my_api;

    public TopNewsAsyncTask(OnTaskCompleted taskCompleted){
        super();
        onTaskCompleted = taskCompleted;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        Map<String, String> options = new HashMap<>();
        options.put("country", "us");
        options.put("apiKey", my_api_key);

        ArticleClient client = ApiUtils.getArticleClient();
        Call<ArticlesResponse> call = client.getTopNews(options);
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
