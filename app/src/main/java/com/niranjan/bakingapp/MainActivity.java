package com.niranjan.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.niranjan.bakingapp.adapters.RecipeAdapter;
import com.niranjan.bakingapp.models.Ingredient;
import com.niranjan.bakingapp.models.Recipe;
import com.niranjan.bakingapp.models.Step;
import com.niranjan.bakingapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPES_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_ID_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_SIZE_PREFERENCE_KEY;
import static com.niranjan.bakingapp.utilities.RecipeUtils.SHARED_PREFERENCE_NAME;

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.ListRecipeItemClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private static String TAG = MainActivity.class.getSimpleName();

    private static final String RECIPE_EXTRA = "recipe";
    private static final String RECIPE_KEY = "recipe";


    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.tv_error_message)
    TextView mDisplayErrorTextView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private List<Recipe>mRecipeArrayList;
    private List<Ingredient>mIngredientArrayList;
    private List<Step>mStepArrayList;

    private RecipeAdapter mAdapter;

    private boolean IS_TABLET = false;

    private static final int RECIPE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mAdapter = new RecipeAdapter(this,this);

        GridLayoutManager gridLayoutManager;
        LinearLayoutManager linearLayoutManager;

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);

        if(findViewById(R.id.tablet_view) == null){
            IS_TABLET =false;
        } else {
            IS_TABLET = true;
        }

        if(IS_TABLET){
            //gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
            mRecipeRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecipeRecyclerView.setLayoutManager(linearLayoutManager);
            } else {
                mRecipeRecyclerView.setLayoutManager(gridLayoutManager);
            }
        }
        mRecipeRecyclerView.setHasFixedSize(true);
        mRecipeRecyclerView.setAdapter(mAdapter);


        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)){
            mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            mAdapter.setRecipeArrayList(mRecipeArrayList);
        } else {
            mRecipeArrayList = new ArrayList<>();
            mStepArrayList = new ArrayList<>();
            mIngredientArrayList = new ArrayList<>();
            makeRecipeRequest();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(RECIPE_KEY,(ArrayList<Recipe>) mRecipeArrayList);
    }


    void makeRecipeRequest(){
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(RECIPE_LOADER_ID);

        if(loader == null){
            loaderManager.initLoader(RECIPE_LOADER_ID,null,this);
        } else {
            loaderManager.restartLoader(RECIPE_LOADER_ID, null, this);
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String recipeCache;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if(recipeCache == null){
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mRecipeArrayList = new ArrayList<>();

                    showRecipeListView();
                    forceLoad();
                } else{
                    deliverResult(recipeCache);
                }

            }

            @Override
            public String loadInBackground() {

                URL recipeUrl = NetworkUtils.buildRecipeURL();

                if(recipeUrl == null){
                    return null;
                }

                String recipeResult = null;

                try{
                    recipeResult = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
                } catch(IOException e){
                    e.printStackTrace();
                    return null;
                }
                return recipeResult;
            }

            @Override
            public void deliverResult(String data) {
                super.deliverResult(data);
                recipeCache = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if(data != null && !TextUtils.isEmpty(data)) {

            try {
                JSONArray recipeJsonArray = new JSONArray(data);

                for(int i=0; i<recipeJsonArray.length();i++){
                    JSONArray ingredientJsonArray = recipeJsonArray.getJSONObject(i).
                            getJSONArray("ingredients");

                    JSONArray stepJsonArray = recipeJsonArray.getJSONObject(i).
                            getJSONArray("steps");

                    mIngredientArrayList = new ArrayList<>();
                    for(int j=0;j<ingredientJsonArray.length();j++){

                        int quantity = ingredientJsonArray.getJSONObject(j).
                                getInt("quantity");
                        String measure = ingredientJsonArray.getJSONObject(j)
                                .getString("measure");
                        String ingredient = ingredientJsonArray.getJSONObject(j).
                                getString("ingredient");
                        mIngredientArrayList.add(new Ingredient(quantity,measure,ingredient));
                    }

                    mStepArrayList = new ArrayList<>();
                    for(int k=0;k<stepJsonArray.length();k++){
                        int stepId = stepJsonArray.getJSONObject(i).getInt("id");
                        String shortDescription = stepJsonArray.getJSONObject(k).
                                getString("shortDescription");
                        String description = stepJsonArray.getJSONObject(k).
                                getString("description");
                        String videoURL = stepJsonArray.getJSONObject(k).
                                getString("videoURL");
                        String thumbnailURL = stepJsonArray.getJSONObject(k).
                                getString("thumbnailURL");
                        mStepArrayList.add(new Step(stepId,
                                shortDescription,
                                description,
                                videoURL,
                                thumbnailURL));
                    }

                    int id = recipeJsonArray.getJSONObject(i).getInt("id");
                    String name = recipeJsonArray.getJSONObject(i).getString("name");
                    int servings = recipeJsonArray.getJSONObject(i).getInt("servings");
                    String image = recipeJsonArray.getJSONObject(i).getString("image");

                    mRecipeArrayList.add(new Recipe(id,
                            name,
                            mIngredientArrayList,
                            mStepArrayList,
                            servings,
                            image));
                }

                showRecipeListView();
                saveToSharedPreference(mRecipeArrayList);
                mAdapter.setRecipeArrayList(mRecipeArrayList);
            } catch(JSONException e){
                e.printStackTrace();
                showErrorMessage();
                mAdapter.setRecipeArrayList(null);
            }

        } else {
            Log.e(TAG, "Error fetching Recipes");
            showErrorMessage();
            mAdapter.setRecipeArrayList(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    void showErrorMessage(){
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mDisplayErrorTextView.setVisibility(View.VISIBLE);
    }

    void showRecipeListView(){
        mDisplayErrorTextView.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method saves Recipe List in SharedPreferences ,which is used by widget
     * to display ingredient list
     * @param mRecipeList Recipe list to be saved in SharedPreferences
     */
    private void saveToSharedPreference(List<Recipe> mRecipeList) {

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String serializedRecipes = gson.toJson(mRecipeList);
        editor.putString(RECIPES_PREFERENCE_KEY, serializedRecipes);
        editor.putInt(RECIPE_ID_PREFERENCE_KEY, 0);
        editor.putInt(RECIPE_SIZE_PREFERENCE_KEY, mRecipeList.size());
        editor.apply();
    }


    @Override
    public void onListRecipeItemClick(Recipe recipeItem) {

        Bundle b = new Bundle();
        b.putParcelable(RECIPE_EXTRA,recipeItem);

        Intent startDetailsActivityIntent = new Intent(this,DetailsActivity.class);
        startDetailsActivityIntent.putExtras(b);
        startActivity(startDetailsActivityIntent);
    }
}
