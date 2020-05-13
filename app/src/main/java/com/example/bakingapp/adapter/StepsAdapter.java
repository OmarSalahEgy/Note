package com.example.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Steps;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    TextView stepTextView;


    public interface StepItemClickListener {
        void onStepItemClick(Steps step);
    }

    StepItemClickListener mOnClickListener;


    ArrayList<Steps> steps;

    public StepsAdapter(ArrayList<Steps> steps, StepItemClickListener clickListener){
        this.steps=steps;
        this.mOnClickListener = clickListener;
    }
    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder  {

        public StepsAdapterViewHolder(View view) {
            super(view);
            stepTextView=(TextView) view.findViewById(R.id.step_text_view);
        }
    }
    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepsAdapterViewHolder holder, final int position) {
        Steps step =steps.get(position);
        stepTextView.setText(step.getShortDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Steps step =steps.get(position);
                mOnClickListener.onStepItemClick(step);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == steps) return 0;
        return steps.size();    }

}
