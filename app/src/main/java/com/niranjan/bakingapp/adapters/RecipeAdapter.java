package com.niranjan.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private List<Recipe> mRecipeArrayList;
    Context mContext;
    final private ListRecipeItemClickListener mOnClickListener;

    public RecipeAdapter(Context context, ListRecipeItemClickListener clickListener){
        mRecipeArrayList = new ArrayList<>();
        mOnClickListener = clickListener;
        mContext = context;

    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflator = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflator.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeViewHolder holder = new RecipeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        if(mRecipeArrayList !=null && mRecipeArrayList.size() > 0) {
            String imageURL = mRecipeArrayList.get(position).mImage;
            if(!(imageURL.equals(""))){
                Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_broken_image).
                        into(holder.mRecipeImage);
            }
            holder.mRecipeDisplayTextView.setText(mRecipeArrayList.get(position).mName);
            holder.mServingDisplayTextView.setText(""+mRecipeArrayList.get(position).mServings);
        }

    }

    @Override
    public int getItemCount() {
        if(mRecipeArrayList == null) return 0;
        else return mRecipeArrayList.size();
    }

    public interface ListRecipeItemClickListener {
        void onListRecipeItemClick(Recipe recipeItem);
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeDisplayTextView;
        @BindView(R.id.tv_servings)
        TextView mServingDisplayTextView;
        @BindView(R.id.iv_recipe)
        ImageView mRecipeImage;

        public RecipeViewHolder(View v){
            super(v);

            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListRecipeItemClick(mRecipeArrayList.get(clickedPosition));
        }
    }

    /**
     * This method is used to set the recipe list on a RecipeAdapter.
     * @param recipeArrayList The new movie reviews list to be displayed.
     */
    public void setRecipeArrayList(List<Recipe> recipeArrayList) {
        this.mRecipeArrayList = recipeArrayList;
        notifyDataSetChanged();
    }
}
