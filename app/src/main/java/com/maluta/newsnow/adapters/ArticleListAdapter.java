package com.maluta.newsnow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.newsnow.R;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.DateConverter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 9/15/2018.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleHolder> {
    private ArrayList<Article> articles = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onClick(ArrayList<Article> articles, int position);
    }

    public ArticleListAdapter(ItemClickListener itemClickListener) {

        mItemClickListener = itemClickListener;
    }

    public void setData (ArrayList<Article> items){
        articles = items;
        notifyDataSetChanged();
    }

    public void addAll (ArrayList<Article> items){
        articles.addAll(items);
        notifyDataSetChanged();
    }

    public void clearAll (){
        articles.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);

        return new ArticleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        final Article article = articles.get(position);
        Context context = holder.itemView.getContext();
        String imageUri = article.getUrlToImage();

        if (!TextUtils.isEmpty(imageUri) || imageUri != null) {
            Picasso.with(context).load(imageUri)
                    .placeholder(R.drawable.ic_newspaper_color)
                    .error(R.drawable.ic_newspaper_color)
                    .into(holder.mArticleIv);
        } else {
            holder.mArticleIv.setImageResource(R.drawable.ic_newspaper_color);
        }

        String articleTitle = article.getTitle();
        holder.mArticleTitleTv.setText(articleTitle);

        holder.mSourcePublisheOnDateTv.setText(Html.fromHtml((article.getSource().getName() == null ? "" : String.format(context.getResources().getString(R.string.source_link), article.getSource().getName(), article.getUrl())) + " " + ((article.getPublishedAt() == null || TextUtils.isEmpty(article.getPublishedAt())) ? "" : String.format(context.getResources().getString(R.string.on_date), DateConverter.dateToShortDateFormat(context, article.getPublishedAt())))));

    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_image)
        ImageView mArticleIv;
        @BindView(R.id.tv_title)
        TextView mArticleTitleTv;
        @BindView(R.id.tv_source_published_on_date)
        TextView mSourcePublisheOnDateTv;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClickListener.onClick(articles, position);
        }
    }
}
