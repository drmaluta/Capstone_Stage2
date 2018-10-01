package com.maluta.newsnow.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maluta.newsnow.R;
import com.maluta.newsnow.adapters.ArticleListAdapter;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.models.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by admin on 9/11/2018.
 */

public class FavoritesFragment extends Fragment implements ArticleListAdapter.ItemClickListener{
    @BindView(R.id.favorites_recycler_view)
    RecyclerView mArticlesRv;

    private ArrayList<Article> mArticles = new ArrayList<>();
    private ArticleListAdapter articleListAdapter;
    private FavoriteArticleListener articleClickListener;
    private MainViewModel viewModel;
    private Observer observer = new Observer<List<Article>>() {
        @Override
        public void onChanged(@Nullable List<Article> articles) {
            if (articles != null) {
                mArticles = (ArrayList<Article>) articles;
            }
            Timber.d("mArticles.size = " + mArticles.size());
            articleListAdapter.setData(articlesToObject(mArticles));
            mArticlesRv.setAdapter(articleListAdapter);

        }
    };
    public static final String ARGUMENT_ARTICLES = "arg.articles";

    public FavoritesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    public interface FavoriteArticleListener {
        void onFavoriteArticleClicked(ArrayList<Article> articles, int  pos);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            articleClickListener = (FavoriteArticleListener) context;
        } catch (ClassCastException ec) {
            throw new ClassCastException(context.toString()
                    + " must implement FavoriteArticleListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

        articleListAdapter = new ArticleListAdapter(this);

        int columns = getResources().getInteger(R.integer.list_column_count);
        GridLayoutManager mGridLayoutManager  = new GridLayoutManager(view.getContext(), columns);
        mArticlesRv.setLayoutManager(mGridLayoutManager);


        return view;
    }

    private ArrayList<Object> articlesToObject(ArrayList<Article> articles){
        return new ArrayList<Object>(articles);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadArticles();
    }


    private void loadArticles() {
        viewModel.getFavoriteArticles().removeObserver(observer);
        viewModel.getFavoriteArticles().observe(this, observer);
    }

    @Override
    public void onClick(ArrayList<Article> articles, int position) {
        articleClickListener.onFavoriteArticleClicked(articles, position);
    }
}
