package com.maluta.newsnow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;
import com.maluta.newsnow.R;
import com.maluta.newsnow.models.Article;

/**
 * Created by admin on 9/28/2018.
 */

public class ArticleListAdapterFree extends ArticleListAdapter {
    private static final int ARTICLE_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;

    public ArticleListAdapterFree(ItemClickListener itemClickListener) {
        super(itemClickListener);
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof Article ? ARTICLE_ITEM_VIEW_TYPE : NATIVE_EXPRESS_AD_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                View bannerLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.banner_ad_item,
                    parent, false);

                return new AdViewHolder(bannerLayoutView);

            case ARTICLE_ITEM_VIEW_TYPE:
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                AdViewHolder bannerHolder = (AdViewHolder) holder;
                AdView adView = (AdView) data.get(position);
                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;

                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                adCardView.addView(adView);
                break;
            case ARTICLE_ITEM_VIEW_TYPE:
            default:
                super.onBindViewHolder(holder, position);
                break;

        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View view) {
            super(view);
        }
    }
}
