package com.maluta.newsnow.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.maluta.newsnow.ArticleDetailsActivity;
import com.maluta.newsnow.R;
import com.maluta.newsnow.database.AppDatabase;
import com.maluta.newsnow.database.AppExecutors;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.DateConverter;
import com.maluta.newsnow.widget.WidgetUpdateService;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsFragment extends Fragment {
    @BindView(R.id.share_fab)
    Button mShareFAB;
    @BindView(R.id.bookmark_fab)
    FloatingActionButton mBookmarkFAB;
    @BindView(R.id.action_up)
    ImageButton mUpButton;
    @BindView(R.id.photo)
    ImageView mArticleImageView;
    @BindView(R.id.article_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_author_published_on_date)
    TextView mAuthorPublishedOnDateTextView;
    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;
    @BindView(R.id.tv_source)
    TextView mSourceTextView;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    public static final String ARGUMENT_ARTICLE = "arg.article";
    private Article mArticle;
    private AppDatabase mDb;
    private Context mContext;
    private boolean mFavorite;
    private FirebaseAnalytics firebaseAnalytics;




    public ArticleDetailsFragment() {
        // Required empty public constructor
    }


    public static ArticleDetailsFragment newInstance(Article article) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_ARTICLE, article);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mArticle = getArguments().getParcelable(ARGUMENT_ARTICLE);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        ButterKnife.bind(this, view);
        initUpButtonOnClickListener();
        initShareOnClickListener();

        mFavorite = mArticle.isFavorite();

        mBookmarkFAB.setImageDrawable(mContext.getResources()
                .getDrawable(mFavorite ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_empty));
        initBookmarkFAB();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        String imageUri = mArticle.getUrlToImage();

        if (!TextUtils.isEmpty(imageUri) || imageUri != null) {
            Picasso.with(mContext).load(imageUri)
                    .placeholder(R.drawable.ic_newspaper_color)
                    .error(R.drawable.ic_newspaper_color)
                    .into(mArticleImageView);
        } else {
            mArticleImageView.setImageResource(R.drawable.ic_newspaper_color);
        }

        mTitleTextView.setText(mArticle.getTitle() == null ? "N/A" : mArticle.getTitle());
        mCollapsingToolbar.setTitle(mArticle.getTitle() == null ? "N/A" : mArticle.getTitle());
        mAuthorPublishedOnDateTextView.setText((mArticle.getAuthor() == null ? "N/A" : String.format(mContext.getResources().getString(R.string.by_author), mArticle.getAuthor())) + " " + (mArticle.getPublishedAt() ==null ? "" : String.format(mContext.getResources().getString(R.string.on_date), DateConverter.dateToShortDateFormat(mContext, mArticle.getPublishedAt()))));
        mDescriptionTextView.setText(mArticle.getDescription());
        mSourceTextView.setText(mArticle.getSource().getName() == null ? "" : Html.fromHtml(String.format(mContext.getResources().getString(R.string.read_more_at_source), mArticle.getSource().getName(), mArticle.getUrl())));
    }



    private void initShareOnClickListener() {
        mShareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(mArticle.getTitle())
                        .getIntent(), getString(R.string.action_share)));

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mArticle.getTitle());
                //Logs an app event.
                firebaseAnalytics.logEvent("SHARE_ARTICLE", bundle);
            }
        });
    }

    private void initBookmarkFAB(){
        mBookmarkFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb = AppDatabase.getInstance(mContext);
                Bundle bundle = new Bundle();
                if (mFavorite) {
                    mFavorite = false;
                    mBookmarkFAB.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_empty));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mArticle.getTitle());
                    //Logs an app event.
                    firebaseAnalytics.logEvent("DELETE_FROM_FAVORITES", bundle);
                    deleteArticle();
                } else {
                    mFavorite = true;
                    mBookmarkFAB.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mArticle.getTitle());
                    //Logs an app event.
                    firebaseAnalytics.logEvent("ADD_TO_FAVORITES", bundle);
                    addArticle();
                }
                WidgetUpdateService.startActionUpdateFavoriteArticlesWidgets(mContext);
            }
        });

    }

    private void addArticle(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mArticle.setFavorite(true);
                mDb.articleDao().insertArticle(mArticle);
            }
        });
    }

    private void deleteArticle(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mArticle.setFavorite(false);
                mDb.articleDao().deleteArticle(mArticle);
            }
        });
    }

    private void initUpButtonOnClickListener(){
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityCast().finish();
            }
        });
    }

    public ArticleDetailsActivity getActivityCast() {
        return (ArticleDetailsActivity) getActivity();
    }
}
