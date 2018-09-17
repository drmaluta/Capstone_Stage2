package com.maluta.newsnow;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.maluta.newsnow.adapters.PagerAdapter;
import com.maluta.newsnow.fragments.AllNewsFragment;
import com.maluta.newsnow.fragments.TopNewsFragment;
import com.maluta.newsnow.models.Article;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TopNewsFragment.ArticleListener,
        AllNewsFragment.AllNewsArticleListener{
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.app_name);

        tabLayout.addTab(tabLayout.newTab().setText("TOP NEWS"));
        tabLayout.addTab(tabLayout.newTab().setText("ALL NEWS"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITES"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onArticleClicked(Article article) {

    }

    @Override
    public void onAllNewsArticleClicked(Article article) {

    }
}
