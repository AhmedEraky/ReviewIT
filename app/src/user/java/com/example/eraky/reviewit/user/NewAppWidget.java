package com.example.eraky.reviewit.user;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.eraky.reviewit.R;


/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Intent intent = new Intent(context, Service.class);
            views.setRemoteAdapter(R.id.listwedg, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAppWidget(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

