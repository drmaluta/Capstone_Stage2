package com.maluta.newsnow.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.maluta.newsnow.utils.TopNewsAsyncTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 9/10/2018.
 */

public class TopNewsFragment extends Fragment implements ArticleListAdapter.ItemClickListener{
    @BindView(R.id.top_news_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mArticlesRv;

    private ArrayList<Article> mArticles = new ArrayList<>();
    private ArticleListAdapter articleListAdapter;
    private ArticleListener articleClickListener;

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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadArticles();
            }
        });

        articleListAdapter = new ArticleListAdapter(this);

        int columns = getResources().getInteger(R.integer.list_column_count);
        GridLayoutManager mGridLayoutManager  = new GridLayoutManager(view.getContext(), columns);
        mArticlesRv.setLayoutManager(mGridLayoutManager);
        loadArticles();

        return view;
    }

    private void loadArticles(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchArticles(ArrayList<Article> articles) {
                mArticles = articles;
                Timber.d("mArticles.size = " + mArticles.size());
                articleListAdapter.setData(mArticles);
                mArticlesRv.setAdapter(articleListAdapter);
                refreshLayout.setRefreshing(false);
            }
        };

        TopNewsAsyncTask articlesTask = new TopNewsAsyncTask(onTaskCompleted);
        articlesTask.execute();
    }

    @Override
    public void onClick(ArrayList<Article> articles, int position) {
        articleClickListener.onArticleClicked(articles, position);
    }
}
