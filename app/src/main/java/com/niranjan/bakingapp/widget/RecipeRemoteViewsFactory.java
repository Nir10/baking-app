package com.niranjan.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Ingredient;
import com.niranjan.bakingapp.models.Recipe;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPES_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_ID_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.SHARED_PREFERENCE_NAME;

/**
 * Created by Niranjan on 17/03/2018.
 */

/*create a list of ingredient in widget*/
public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Ingredient> mIngredientsList;

    public RecipeRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        mIngredientsList = getIngredientsFromPreferences(context);
    }

    public List<Ingredient> getIngredientsFromPreferences(Context context) {
        SharedPreferences SharedPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        int recipeId = SharedPrefs.getInt(RECIPE_ID_PREFERENCE_KEY, 0);

        String jsonString = SharedPrefs.getString(RECIPES_PREFERENCE_KEY, null);
        Type type = new TypeToken<List<Recipe>>() {
        }.getType();
        Gson gson = new Gson();
        List<Recipe> recipes = gson.fromJson(jsonString, type);

        return recipes.get(recipeId).mIngredients;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientsList == null) return 0;
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredientsList == null) return null;
        Ingredient item = mIngredientsList.get(position);

        RemoteViews ingredientItemView = new RemoteViews(context.getPackageName(),
                R.layout.widget_ingredient_list_item);
        ingredientItemView.setTextViewText(R.id.tv_quantity, ""+item.mQuantity);
        ingredientItemView.setTextViewText(R.id.tv_measure, item.mMeasure);
        ingredientItemView.setTextViewText(R.id.tv_ingredient, item.mIngredient);

        return ingredientItemView;
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
        return false;
    }
}
