package com.niranjan.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    private List<Ingredient> mIngredientArrayList;
    Context mContext;

    public IngredientAdapter(Context context){
        mIngredientArrayList = new ArrayList<>();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflator = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflator.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        IngredientViewHolder holder = new IngredientViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {

        if(mIngredientArrayList !=null && mIngredientArrayList.size() > 0) {
            holder.mIngredientDisplayTextView.setText(mIngredientArrayList.get(position).mIngredient);
            holder.mMeasureDisplayTextView.setText(""+mIngredientArrayList.get(position).mMeasure);
            holder.mQuantityDisplayTextView.setText(""+mIngredientArrayList.get(position).mQuantity);
            if(position == mIngredientArrayList.size()-1){
                //hide last items divider line
                holder.mDividerIngredient.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mIngredientArrayList == null)return 0;
        else return mIngredientArrayList.size();
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_ingredient)
        TextView mIngredientDisplayTextView;
        @BindView(R.id.tv_quantity)
        TextView mQuantityDisplayTextView;
        @BindView(R.id.tv_measure)
        TextView mMeasureDisplayTextView;
        @BindView(R.id.divider_ingredient)
        View mDividerIngredient;

        public IngredientViewHolder(View v){
            super(v);

            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

        }
    }
    /**
     * This method is used to set the ingredient list on a IngredientAdapter.
     * @param ingredientArrayList The new ingredient list to be displayed.
     */
    public void setIngredientArrayList(List<Ingredient> ingredientArrayList) {
        this.mIngredientArrayList = ingredientArrayList;
        notifyDataSetChanged();
    }
}
