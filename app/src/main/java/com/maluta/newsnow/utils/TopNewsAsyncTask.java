package com.maluta.newsnow.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;

import com.maluta.newsnow.BuildConfig;
import com.maluta.newsnow.R;
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
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private int mPage;
    private static String my_api_key = BuildConfig.my_api;

    public TopNewsAsyncTask(OnTaskCompleted taskCompleted, Context context,int page){
        super();
        onTaskCompleted = taskCompleted;
        mContext = context;
        mPage = page;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String language = sharedPreferences.getString(mContext.getString(R.string.pref_language_key),
                mContext.getString(R.string.pref_language_english_value));

        String sources = sharedPreferences.getString(mContext.getString(R.string.pref_sources_key),
                mContext.getString(R.string.pref_sources_all_value));

        Map<String, String> options = new HashMap<>();
        if ((!language.equals("en") && sources.equals("all")) || (!language.equals("en") && !sources.equals("all"))) {
            options.put("language", language);
        } else if (language.equals("en") && !sources.equals("all")) {
            options.put("sources", sources);
        } else if (language.equals("en") && sources.equals("all")) {
            options.put("language", language);
        }
        //options.put("page", String.valueOf(mPage));
        //options.put("apiKey", my_api_key);

        ArticleClient client = ApiUtils.getArticleClient();
        Call<ArticlesResponse> call = client.getTopNews(options, String.valueOf(mPage), my_api_key);
        try {
            Response<ArticlesResponse> rp = call.execute();
            return rp.body().getResults();
        } catch (IOException e) {
            Timber.e("error in getting response from service using retrofit");
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
