package com.niranjan.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.niranjan.bakingapp.fragments.StepDescriptionFragment;
import com.niranjan.bakingapp.models.Recipe;
import com.niranjan.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import static com.niranjan.bakingapp.utilities.RecipeUtils.POSITION_EXTRA;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_EXTRA;
import static com.niranjan.bakingapp.utilities.RecipeUtils.STEP_EXTRA;

public class DescriptionActivity extends AppCompatActivity implements
        StepDescriptionFragment.OnFragmentInteractionListener {



    Step mStep;
    int position;
    List<Step> mStepArrayList;
    Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if(intent.hasExtra(STEP_EXTRA)){
            mStep = intent.getParcelableExtra(STEP_EXTRA);
            position = (int) intent.getIntExtra(POSITION_EXTRA,0);
            mRecipe = intent.getParcelableExtra(RECIPE_EXTRA);

            mStepArrayList = (ArrayList<Step>) mRecipe.mSteps;
            getSupportActionBar().setTitle(mRecipe.mName);

            if(savedInstanceState == null) {
                StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

                stepDescriptionFragment.setStepArrayList((ArrayList<Step>) mStepArrayList);
                stepDescriptionFragment.setListIndex(position);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.step_description_container, stepDescriptionFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(int position) {

        StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

        stepDescriptionFragment.setStepArrayList((ArrayList<Step>) mStepArrayList);
        stepDescriptionFragment.setListIndex(position);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.step_description_container, stepDescriptionFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
