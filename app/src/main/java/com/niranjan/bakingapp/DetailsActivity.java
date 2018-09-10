package com.niranjan.bakingapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.niranjan.bakingapp.adapters.IngredientAdapter;
import com.niranjan.bakingapp.adapters.StepAdapter;
import com.niranjan.bakingapp.fragments.StepDescriptionFragment;
import com.niranjan.bakingapp.models.Ingredient;
import com.niranjan.bakingapp.models.Recipe;
import com.niranjan.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String RECIPE_EXTRA = "recipe";

    private Recipe mRecipe;

    public static boolean IS_TABLET = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.description_container) == null){
            IS_TABLET =false;
        } else {
            IS_TABLET = true;
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(RECIPE_EXTRA)) {
            mRecipe = intent.getParcelableExtra(RECIPE_EXTRA);
            getSupportActionBar().setTitle(mRecipe.mName);
        }

        if (DetailsActivity.IS_TABLET) {

            if(savedInstanceState == null) {
                StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

                stepDescriptionFragment.setStepArrayList((ArrayList<Step>) mRecipe.mSteps);
                stepDescriptionFragment.setListIndex(0);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction()
                        .add(R.id.description_container, stepDescriptionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
