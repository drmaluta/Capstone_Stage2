package com.maluta.newsnow.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.maluta.newsnow.models.Article;

import java.util.List;

/**
 * Created by admin on 9/21/2018.
 */

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article")
    LiveData<List<Article>> loadAllArticles();

    @Query("SELECT * FROM article")
    List<Article> getAllArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);
}
