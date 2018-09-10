package com.niranjan.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private List<Step> mStepArrayList;
    Context mContext;
    final private ListStepItemClickListener mOnClickListener;

    public StepAdapter(Context context, ListStepItemClickListener clickListener){
        mStepArrayList = new ArrayList<>();
        mOnClickListener = clickListener;

    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflator = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;

        View view = inflator.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepViewHolder holder = new StepViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        if(mStepArrayList !=null && mStepArrayList.size() > 0) {
            String stepNumber = "";
            if(position != 0){
                stepNumber = position+". ";
            }

            holder.mShortDescriptionDisplayTextView.setText(stepNumber+mStepArrayList.get(position).
                    mShortDescription);
            if(position == mStepArrayList.size()-1){
                holder.mDividerStep.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        if(mStepArrayList == null)return 0;
        else return mStepArrayList.size();
    }

    public interface ListStepItemClickListener {
        void onListStepItemClick(Step StepItem,int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_short_description)
        TextView mShortDescriptionDisplayTextView;
        @BindView(R.id.divider_step)
        View mDividerStep;

        public StepViewHolder(View v){
            super(v);

            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListStepItemClick(mStepArrayList.get(clickedPosition),
                    clickedPosition);
        }
    }
    /**
     * This method is used to set the step list on a stepAdapter.
     * @param stepArrayList The new step list to be displayed.
     */
    public void setStepArrayList(List<Step> stepArrayList) {
        this.mStepArrayList = stepArrayList;
        notifyDataSetChanged();
    }
}
