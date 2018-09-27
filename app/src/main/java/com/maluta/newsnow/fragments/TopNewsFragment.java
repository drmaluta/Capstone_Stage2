package com.maluta.newsnow.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maluta.newsnow.R;
import com.maluta.newsnow.adapters.ArticleListAdapter;
import com.maluta.newsnow.interfaces.OnTaskCompleted;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.models.MainViewModel;
import com.maluta.newsnow.utils.PaginationScrollListener;
import com.maluta.newsnow.utils.TopNewsAsyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 9/10/2018.
 */

public class TopNewsFragment extends Fragment implements ArticleListAdapter.ItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.top_news_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mArticlesRv;

    private ArrayList<Article> mArticles = new ArrayList<>();
    private ArticleListAdapter articleListAdapter;
    private ArticleListener articleClickListener;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    public interface ArticleListener {
        void onArticleClicked(ArrayList<Article> articles, int  pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            articleClickListener = (ArticleListener) context;
        } catch (ClassCastException ec) {
            throw new ClassCastException(context.toString()
                    + " must implement ArticleListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_news, container, false);
        ButterKnife.bind(this, view);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .registerOnSharedPreferenceChangeListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = PAGE_START;
                articleListAdapter.clearAll();
                loadFirstPage();
            }
        });

        articleListAdapter = new ArticleListAdapter(this);
        int columns = getResources().getInteger(R.integer.list_column_count);
        GridLayoutManager mGridLayoutManager  = new GridLayoutManager(view.getContext(), columns);
        mArticlesRv.setLayoutManager(mGridLayoutManager);
        mArticlesRv.setAdapter(articleListAdapter);
        mArticlesRv.addOnScrollListener(new PaginationScrollListener(mGridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();

        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Context context = getActivity().getApplicationContext();
        if (key.equals(context.getString(R.string.pref_language_key)) || key.equals(context.getString(R.string.pref_sources_key))){
            currentPage = PAGE_START;
            articleListAdapter.clearAll();
            loadFirstPage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void loadFirstPage(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchArticles(ArrayList<Article> articles) {
                if (!articles.isEmpty()) {
                    mArticles = articles;
                    Timber.d("mArticles.size = " + mArticles.size());
                    articleListAdapter.setData(mArticles);
                    refreshLayout.setRefreshing(false);
                }
                if (currentPage == TOTAL_PAGES || articles.isEmpty()){
                    isLastPage = true;
                }
            }
        };

        TopNewsAsyncTask articlesTask = new TopNewsAsyncTask(onTaskCompleted, getActivity().getApplicationContext(), currentPage);
        articlesTask.execute();
    }

    private void loadNextPage(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchArticles(ArrayList<Article> articles) {
                if (!articles.isEmpty()) {
                    isLoading = false;
                    mArticles = articles;
                    Timber.d("mArticles.size = " + mArticles.size());
                    articleListAdapter.addAll(mArticles);
                    refreshLayout.setRefreshing(false);
                }
                if (currentPage == TOTAL_PAGES || articles.isEmpty()){
                    isLastPage = true;
                }
            }
        };
        TopNewsAsyncTask articlesTask = new TopNewsAsyncTask(onTaskCompleted, getActivity().getApplicationContext(), currentPage);
        articlesTask.execute();
    }


    @Override
    public void onClick(ArrayList<Article> articles, int position) {
        articleClickListener.onArticleClicked(articles, position);
    }
}
