package com.maluta.newsnow.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.maluta.newsnow.R;
import com.maluta.newsnow.adapters.ArticleListAdapterFree;
import com.maluta.newsnow.models.Article;

import java.util.ArrayList;

/**
 * Created by admin on 9/29/2018.
 */

public class AllNewsFragmentFree extends AllNewsFragment {
    public static int ITEMS_PER_AD = 0;
    private static final String AD_UNIT_ID = "ca-app-pub-6397027704907885/9676888433";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ITEMS_PER_AD = getContext().getResources().getInteger(R.integer.items_pre_ad);
        View result = super.onCreateView(inflater, container, savedInstanceState);
        mArticlesRv.setAdapter(articleListAdapter = new ArticleListAdapterFree(this));
        return result;
    }

    /**
     * Adds Banner ads to the items list.
     */
    private void addBannerAds(ArrayList<Object> items) {
        for (int i = 0; i <= items.size(); i += ITEMS_PER_AD) {
            final AdView adView = new AdView(getContext());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(AD_UNIT_ID);
            items.add(i, adView);
        }
    }

    /**
     * Sets up and loads the Banner ads.
     */
    private void setUpAndLoadBannerAds(final ArrayList<Object> items) {
        mArticlesRv.post(new Runnable() {
            @Override
            public void run() {

                loadBannerAd(0, items);
            }
        });
    }

    /**
     * Loads the Banner ads in the items list.
     */
    private void loadBannerAd(final int index, final ArrayList<Object> items) {
        if (index >= items.size()) {
            return;
        }

        Object item = items.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad"
                    + " ad.");
        }

        final AdView adView = (AdView) item;

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loadBannerAd(index + ITEMS_PER_AD, items);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadBannerAd(index + ITEMS_PER_AD, items);
            }
        });

        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void setAdapterData(ArrayList<Article> articles) {
        if (articles != null || !articles.isEmpty()) {
            ArrayList<Object> items = new ArrayList<Object>(articles);
            addBannerAds(items);
            setUpAndLoadBannerAds(items);
            articleListAdapter.setData(items);
        }
    }

    @Override
    protected void addAdapterData(ArrayList<Article> articles) {
        if (articles != null || !articles.isEmpty()) {
            ArrayList<Object> items = new ArrayList<Object>(articles);
            addBannerAds(items);
            setUpAndLoadBannerAds(items);
            articleListAdapter.addAll(items);
        }
    }
}
