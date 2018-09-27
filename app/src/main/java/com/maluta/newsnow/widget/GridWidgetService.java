package com.maluta.newsnow.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.maluta.newsnow.R;
import com.maluta.newsnow.database.AppDatabase;
import com.maluta.newsnow.database.AppExecutors;
import com.maluta.newsnow.models.Article;
import com.maluta.newsnow.utils.ArticlesAsyncTaskLoader;
import com.maluta.newsnow.utils.DateConverter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 9/26/2018.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext()) ;
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String EXTRA_ARTICLES = "extra.articles";
    private static final String EXTRA_SELECTED_INDEX = "extra.selected_index";
    private Context mContext;
    private List<Article> mArticles;
    //private AppDatabase mDb;

    public GridRemoteViewsFactory(Context context){
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onDataSetChanged() {
        final AppDatabase mDb = AppDatabase.getInstance(mContext);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mArticles = mDb.articleDao().getAllArticles();
            }
        });

        /*new ArticlesAsyncTaskLoader(mContext){
            @Override
            public void deliverResult(List<Article> data) {
                super.deliverResult(data);
                GridRemoteViewsFactory.this.mArticles = data;
            }
        }.startLoading()*/;
    }

    @Override
    public void onDestroy() {
        //mArticles = null;
    }

    @Override
    public int getCount() {
        if (mArticles == null)
            return 0;
        return mArticles.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mArticles == null){
            return null;
        }
        Article article = mArticles.get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.articles_widget_list_item);
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(mContext).load(article.getUrlToImage()).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Article image
        if (bitmap == null) {
            remoteViews.setImageViewResource(R.id.widget_image_iv, R.drawable.ic_newspaper_color);
        } else {
            remoteViews.setImageViewBitmap(R.id.widget_image_iv, bitmap);
        }

        //title
        remoteViews.setTextViewText(R.id.widget_title_tv, article.getTitle());

        // RSource and published date
        String sourcePublishedOnDate = (article.getSource().getName() == null ? "" : article.getSource().getName())
                + " " +  (article.getPublishedAt()==null ? "" :
                mContext.getResources().getString(R.string.on_date, DateConverter.dateToShortDateFormat(mContext, article.getPublishedAt())));
        remoteViews.setTextViewText(R.id.widget_source_published_on_date_tv, sourcePublishedOnDate);

        // Pending intent when click on view
        Intent fillInIntent = new Intent();
        fillInIntent.putParcelableArrayListExtra(EXTRA_ARTICLES, new ArrayList<Parcelable>(mArticles));
        fillInIntent.putExtra(EXTRA_SELECTED_INDEX, mArticles.indexOf(article));
        remoteViews.setOnClickFillInIntent(R.id.widget_list_item_view, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
