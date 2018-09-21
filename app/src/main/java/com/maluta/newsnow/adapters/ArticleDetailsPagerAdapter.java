package com.maluta.newsnow.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maluta.newsnow.fragments.ArticleDetailsFragment;
import com.maluta.newsnow.models.Article;

import java.util.ArrayList;

/**
 * Created by admin on 9/17/2018.
 */

public class ArticleDetailsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Article> articles;


    public ArticleDetailsPagerAdapter(FragmentManager fm, ArrayList<Article> articles) {
        super(fm);
        setArticless(articles);
    }

    public void setArticless(@NonNull ArrayList<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailsFragment.newInstance(articles.get(position));
    }

    @Override
    public int getCount() {
        return articles.size();
    }
}
