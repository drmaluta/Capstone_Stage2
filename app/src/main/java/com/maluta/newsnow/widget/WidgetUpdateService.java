package com.maluta.newsnow.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.maluta.newsnow.R;
import com.maluta.newsnow.models.Article;

import java.util.ArrayList;

/**
 * Created by admin on 9/25/2018.
 */

public class WidgetUpdateService extends IntentService {
    public static final String ACTION_UPDATE_FAVORITE_ARTICLES_WIDGET = "update_widget_list";
    private static final String ARTICLES = "articles";


    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent !=null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_FAVORITE_ARTICLES_WIDGET.equals(action)) {
                handleActionUpdateFavoriteArticlesWidgets();
            }
        }
    }

    public static void startActionUpdateFavoriteArticlesWidgets(Context context) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_FAVORITE_ARTICLES_WIDGET);
        context.startService(intent);
    }

    private void handleActionUpdateFavoriteArticlesWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoriteArticleWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        FavoriteArticleWidget.updateWidgets(this, appWidgetManager, appWidgetIds);
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
    }
}
