package com.maluta.newsnow;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.maluta.newsnow.adapters.PagerAdapter;
import com.maluta.newsnow.fragments.AllNewsFragment;
import com.maluta.newsnow.fragments.FavoritesFragment;
import com.maluta.newsnow.fragments.TopNewsFragment;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.models.MainViewModel;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements TopNewsFragment.ArticleListener,
        AllNewsFragment.AllNewsArticleListener, FavoritesFragment.FavoriteArticleListener{
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    public static final String EXTRA_ARTICLES = "extra.articles";
    public static final String EXTRA_SELECTED_INDEX = "extra.selected_index";
    private ArrayList<Article> mFavoriteArticles = new ArrayList<>();
    private ArrayList<Article> mArticles = new ArrayList<>();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our articles_menu layout to this menu */
        inflater.inflate(R.menu.articles_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteArticles() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavoriteArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    mFavoriteArticles = (ArrayList<Article>) articles;
                }
                Timber.d("mArticles.size = " + mFavoriteArticles.size());
            }
        });
    }

    private void setFavorite(ArrayList<Article> articles){
        if (mFavoriteArticles != null) {
            for (int i = 0; i < articles.size(); i++){
                for (int j = 0; j < mFavoriteArticles.size(); j++){
                    if (articles.get(i).getTitle().equals(mFavoriteArticles.get(j).getTitle())) {
                        articles.get(i).setFavorite(true);
                        articles.get(i).setId(mFavoriteArticles.get(j).getId());
                    }
                }
            }
        }
    }

    @Override
    public void onArticleClicked(ArrayList<Article> articles, int position) {
        mArticles = articles;
        loadFavoriteArticles();
        setFavorite(mArticles);
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ARTICLES, mArticles);
        intent.putExtra(EXTRA_SELECTED_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onAllNewsArticleClicked(ArrayList<Article> articles, int position) {
        mArticles = articles;
        loadFavoriteArticles();
        setFavorite(mArticles);
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ARTICLES, mArticles);
        intent.putExtra(EXTRA_SELECTED_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onFavoriteArticleClicked(ArrayList<Article> articles, int pos) {
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ARTICLES, articles);
        intent.putExtra(EXTRA_SELECTED_INDEX, pos);
        startActivity(intent);
    }
}
