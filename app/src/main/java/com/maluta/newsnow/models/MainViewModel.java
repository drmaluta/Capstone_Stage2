package com.maluta.newsnow.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.maluta.newsnow.database.AppDatabase;

import java.util.List;

import timber.log.Timber;

/**
 * Created by admin on 9/21/2018.
 */

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Article>> favoriteArticles;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Timber.d("Actively retrieving articles from the DataBase");
        favoriteArticles = database.articleDao().loadAllArticles();
    }

    public LiveData<List<Article>> getFavoriteArticles() {
        return favoriteArticles;
    }
}
