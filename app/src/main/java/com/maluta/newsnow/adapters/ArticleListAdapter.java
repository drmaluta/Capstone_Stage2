package com.maluta.newsnow.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.newsnow.R;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.DateConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 9/15/2018.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected ArrayList<Object> data = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onClick(ArrayList<Article> articles, int position);
    }

    public ArticleListAdapter(ItemClickListener itemClickListener) {

        mItemClickListener = itemClickListener;
    }

    public void setData (ArrayList<Object> items){
        data = items;
        notifyDataSetChanged();
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void addAll (ArrayList<Object> items){
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void clearAll (){
        data.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);

        return new ArticleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Article article = (Article)data.get(position);
        Context context = holder.itemView.getContext();
        String imageUri = article.getUrlToImage();

        if (!TextUtils.isEmpty(imageUri) || imageUri != null) {
            Picasso.with(context).load(imageUri)
                    .placeholder(R.drawable.ic_newspaper_color)
                    .error(R.drawable.ic_newspaper_color)
                    .resize(320, 210)
                    .into(((ArticleHolder)holder).mArticleIv);
        } else {
            ((ArticleHolder)holder).mArticleIv.setImageResource(R.drawable.ic_newspaper_color);
        }

        String articleTitle = article.getTitle();
        ((ArticleHolder)holder).mArticleTitleTv.setText(articleTitle);

        ((ArticleHolder)holder).mSourcePublishedOnDateTv.setText(Html.fromHtml((article.getSource().getName() == null ? "" : String.format(context.getResources().getString(R.string.source_link), article.getSource().getName(), article.getUrl())) + " " + ((article.getPublishedAt() == null || TextUtils.isEmpty(article.getPublishedAt())) ? "" : String.format(context.getResources().getString(R.string.on_date), DateConverter.dateToShortDateFormat(context, article.getPublishedAt())))));

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private ArrayList<Article> getArticlesData() {
        ArrayList<Article> result = new ArrayList<>();
        for (Object o : data) {
            if (o instanceof Article)
                result.add((Article) o);
        }
        return result;
    }

    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_image)
        ImageView mArticleIv;
        @BindView(R.id.tv_title)
        TextView mArticleTitleTv;
        @BindView(R.id.tv_source_published_on_date)
        TextView mSourcePublishedOnDateTv;

        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Article article = (Article)data.get(position);
            ArrayList<Article> articles = getArticlesData();
            mItemClickListener.onClick(getArticlesData(), articles.indexOf(article));
        }
    }
}
