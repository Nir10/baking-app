package com.niranjan.bakingapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niranjan.bakingapp.DescriptionActivity;
import com.niranjan.bakingapp.DetailsActivity;
import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.adapters.IngredientAdapter;
import com.niranjan.bakingapp.adapters.StepAdapter;
import com.niranjan.bakingapp.models.Ingredient;
import com.niranjan.bakingapp.models.Recipe;
import com.niranjan.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.niranjan.bakingapp.DetailsActivity.IS_TABLET;
import static com.niranjan.bakingapp.utilities.RecipeUtils.POSITION_EXTRA;
import static com.niranjan.bakingapp.utilities.RecipeUtils.RECIPE_EXTRA;
import static com.niranjan.bakingapp.utilities.RecipeUtils.STEP_EXTRA;

public class DetailsFragment extends Fragment implements StepAdapter.ListStepItemClickListener {

    private static final String TAG = DetailsFragment.class.getSimpleName();

    private Recipe mRecipe;
    private List<Ingredient> mIngredientArrayList;
    private List<Step> mStepArrayList;

    private StepAdapter mStepAdapter;
    private IngredientAdapter mIngredientAdapter;

    @BindView(R.id.rv_ingredients_list)
    RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.rv_steps)
    RecyclerView mStepRecyclerView;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this,rootView);

        mIngredientArrayList = new ArrayList<>();
        mStepArrayList = new ArrayList<>();

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(RECIPE_EXTRA)) {

            mRecipe = intent.getParcelableExtra(RECIPE_EXTRA);

            LinearLayoutManager linearLayoutManager;

            linearLayoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);

            mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false));
            mIngredientsRecyclerView.setHasFixedSize(true);
            mIngredientAdapter = new IngredientAdapter(getActivity());
            mIngredientsRecyclerView.setAdapter(mIngredientAdapter);
            mIngredientArrayList = mRecipe.mIngredients;
            mIngredientAdapter.setIngredientArrayList(mIngredientArrayList);

            mStepRecyclerView.setLayoutManager(linearLayoutManager);
            mStepRecyclerView.setHasFixedSize(true);
            mStepAdapter = new StepAdapter(getActivity(), this);
            mStepArrayList = (ArrayList<Step>) mRecipe.mSteps;
            mStepRecyclerView.setAdapter(mStepAdapter);
            mStepAdapter.setStepArrayList(mStepArrayList);
            Log.d(TAG, "" + mStepArrayList);
        }

        return rootView;
    }

    @Override
    public void onListStepItemClick(Step StepItem, int position) {

        if(!IS_TABLET) {
            Intent startDescriptionActivityIntent = new Intent(getActivity(),
                    DescriptionActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(STEP_EXTRA, StepItem);
            b.putParcelable(RECIPE_EXTRA, mRecipe);
            b.putInt(POSITION_EXTRA, position);

            startDescriptionActivityIntent.putExtras(b);
            startActivity(startDescriptionActivityIntent);
        } else {

            StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

            stepDescriptionFragment.setStepArrayList((ArrayList<Step>) mStepArrayList);
            stepDescriptionFragment.setListIndex(position);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            fragmentManager.popBackStack();
            fragmentManager.beginTransaction()
                    .replace(R.id.description_container, stepDescriptionFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }
}
