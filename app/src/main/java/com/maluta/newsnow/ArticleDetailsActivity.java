package com.maluta.newsnow;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maluta.newsnow.adapters.ArticleDetailsPagerAdapter;
import com.maluta.newsnow.models.Article;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsActivity extends AppCompatActivity {
    @BindView(R.id.article_details_pager)
    ViewPager viewPager;

    private ArrayList<Article> articles;
    public static final String EXTRA_ARTICLES = "extra.articles";
    public static final String EXTRA_SELECTED_INDEX = "extra.selected_index";
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        articles = intent.getParcelableArrayListExtra(EXTRA_ARTICLES);
        if (savedInstanceState == null) {
            selectedIndex = intent.getIntExtra(EXTRA_SELECTED_INDEX, 0);
        } else {
            selectedIndex = savedInstanceState.getInt(EXTRA_SELECTED_INDEX);
        }
        viewPager.setAdapter(new ArticleDetailsPagerAdapter(getSupportFragmentManager(), articles));
        setUpViewPagerListener();
        viewPager.setCurrentItem(selectedIndex);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECTED_INDEX, selectedIndex);
    }

    private void setUpViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}
