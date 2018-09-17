package com.maluta.newsnow.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maluta.newsnow.R;
import com.maluta.newsnow.adapters.ArticleListAdapter;
import com.maluta.newsnow.interfaces.OnTaskCompleted;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.AllNewsAsyncTask;
import com.maluta.newsnow.utils.PaginationScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 9/11/2018.
 */

public class AllNewsFragment extends Fragment implements ArticleListAdapter.ItemClickListener{
    @BindView(R.id.all_news_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.all_news_rv)
    RecyclerView mArticlesRv;

    private ArrayList<Article> mArticles = new ArrayList<>();
    private ArticleListAdapter articleListAdapter;
    private AllNewsArticleListener articleClickListener;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    public interface AllNewsArticleListener {
        void onAllNewsArticleClicked(Article article);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            articleClickListener = (AllNewsArticleListener) context;
        } catch (ClassCastException ec) {
            throw new ClassCastException(context.toString()
                    + " must implement ArticleListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_news, container, false);
        ButterKnife.bind(this, view);

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

    private void loadFirstPage(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchArticles(ArrayList<Article> articles) {
                mArticles = articles;
                Timber.d("mArticles.size = " + mArticles.size());
                articleListAdapter.setData(mArticles);
                refreshLayout.setRefreshing(false);
                if (currentPage == TOTAL_PAGES){
                    isLastPage = true;
                }
            }
        };
        AllNewsAsyncTask articlesTask = new AllNewsAsyncTask(onTaskCompleted, currentPage);
        articlesTask.execute();
    }

    private void loadNextPage(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchArticles(ArrayList<Article> articles) {
                isLoading = false;
                mArticles = articles;
                Timber.d("mArticles.size = " + mArticles.size());
                articleListAdapter.addAll(mArticles);
                refreshLayout.setRefreshing(false);
                if (currentPage == TOTAL_PAGES){
                    isLastPage = true;
                }
            }
        };
        AllNewsAsyncTask articlesTask = new AllNewsAsyncTask(onTaskCompleted, currentPage);
        articlesTask.execute();
    }

    @Override
    public void onClick(Article article) {
        articleClickListener.onAllNewsArticleClicked(article);
    }
}