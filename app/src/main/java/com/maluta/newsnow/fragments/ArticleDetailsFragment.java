package com.maluta.newsnow.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.maluta.newsnow.ArticleDetailsActivity;
import com.maluta.newsnow.R;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.DateConverter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsFragment extends Fragment {
    @BindView(R.id.share_fab)
    Button mShareFAB;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        ButterKnife.bind(this, view);
        initUpButtonOnClickListener();
        initShareOnClickListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mArticle = getArguments().getParcelable(ARGUMENT_ARTICLE);
        }
        Context context = getActivity().getApplicationContext();
        String imageUri = mArticle.getUrlToImage();

        if (!TextUtils.isEmpty(imageUri) || imageUri != null) {
            Picasso.with(context).load(imageUri)
                    .placeholder(R.drawable.ic_newspaper_color)
                    .error(R.drawable.ic_newspaper_color)
                    .into(mArticleImageView);
        } else {
            mArticleImageView.setImageResource(R.drawable.ic_newspaper_color);
        }

        mTitleTextView.setText(mArticle.getTitle() == null ? "N/A" : mArticle.getTitle());
        mCollapsingToolbar.setTitle(mArticle.getTitle() == null ? "N/A" : mArticle.getTitle());
        mAuthorPublishedOnDateTextView.setText((mArticle.getAuthor() == null ? "N/A" : String.format(context.getResources().getString(R.string.by_author), mArticle.getAuthor())) + " " + (mArticle.getPublishedAt() ==null ? "" : String.format(context.getResources().getString(R.string.on_date), DateConverter.dateToShortDateFormat(context, mArticle.getPublishedAt()))));
        mDescriptionTextView.setText(mArticle.getDescription());
        mSourceTextView.setText(mArticle.getSource().getName() == null ? "" : Html.fromHtml(String.format(context.getResources().getString(R.string.read_more_at_source), mArticle.getSource().getName(), mArticle.getUrl())));
    }


    private void initShareOnClickListener() {
        mShareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(mArticle.getTitle())
                        .getIntent(), getString(R.string.action_share)));
            }
        });
    }

    private void initUpButtonOnClickListener(){
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityCast().onSupportNavigateUp();
            }
        });
    }

    public ArticleDetailsActivity getActivityCast() {
        return (ArticleDetailsActivity) getActivity();
    }







}
