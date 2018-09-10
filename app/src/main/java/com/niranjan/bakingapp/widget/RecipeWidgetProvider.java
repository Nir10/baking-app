package com.niranjan.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Recipe;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPES_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_ID_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_SIZE_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.SHARED_PREFERENCE_NAME;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_WIDGET_PREV =
            "com.niranjan.bakingapp.widget.RecipeWidgetProvider.ACTION_WIDGET_PREV";
    private static final String ACTION_WIDGET_NEXT =
            "com.niranjan.bakingapp.widget.RecipeWidgetProvider.ACTION_WIDGET_NEXT";

    RemoteViews widgetViews;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        updateRecipeId(context,intent);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateRecipeId(Context context,Intent intent) {
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPrefs.edit();
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);
        int recipesSize = SharedPrefs.getInt(RECIPE_SIZE_PREFERENCE_KEY, 1);


        if(intent.getAction().equals(ACTION_WIDGET_NEXT)){
            if (recipeId < recipesSize - 1) {
                recipeId++;
            } else {
                recipeId = 0;
            }
        } else if(intent.getAction().equals(ACTION_WIDGET_PREV)){
            if(recipeId > 0){
                recipeId--;
            } else {
                recipeId = recipesSize-1;
            }
        }


        editor.putInt(RECIPE_ID_PREFERENCE_KEY, recipeId);
        editor.apply();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        widgetViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        String title = getIngredientsTitle(context);
        widgetViews.setTextViewText(R.id.tv_widget_title, title);

        Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);
        widgetViews.setRemoteAdapter(R.id.lv_widget_list, intent);


        Intent nextRecipeIntent = new Intent();

        nextRecipeIntent.setAction(ACTION_WIDGET_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                                                                context,
                                                                0,
                                                                nextRecipeIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
        widgetViews.setOnClickPendingIntent(R.id.iv_widget_next, nextPendingIntent);

        Intent prevRecipeIntent = new Intent();
        prevRecipeIntent.setAction(ACTION_WIDGET_PREV);

        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context,
                0,
                prevRecipeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        widgetViews.setOnClickPendingIntent(R.id.iv_widget_prev,prevPendingIntent);


        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);
        int recipesSize = SharedPrefs.getInt(RECIPE_SIZE_PREFERENCE_KEY, 1);

        if(recipeId == 0){
            widgetViews.setViewVisibility(R.id.iv_widget_prev, View.INVISIBLE);
            widgetViews.setViewVisibility(R.id.iv_widget_next,View.VISIBLE);
        } else if(recipeId == recipesSize -1){
            widgetViews.setViewVisibility(R.id.iv_widget_prev, View.VISIBLE);
            widgetViews.setViewVisibility(R.id.iv_widget_next,View.INVISIBLE);
        } else {
            widgetViews.setViewVisibility(R.id.iv_widget_prev, View.VISIBLE);
            widgetViews.setViewVisibility(R.id.iv_widget_next,View.VISIBLE);
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_list);
    }

    public String getIngredientsTitle(Context context) {
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);

        String jsonString = SharedPrefs.getString(RECIPES_PREFERENCE_KEY, null);
        Type type = new TypeToken<List<Recipe>>() {}.getType();
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(jsonString , type);

        return recipes.get(recipeId).mName;
    }

}

