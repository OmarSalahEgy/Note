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

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    TextView ingredientTextView;
    TextView measureTextView;
    TextView quantityTextView;



    private ArrayList<Ingredients> mIngredients;

    public IngredientsAdapter() {
    }
    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder  {

        public IngredientsAdapterViewHolder(View view) {
            super(view);
            ingredientTextView = (TextView) view.findViewById(R.id.ingredint_text_view);
            measureTextView = (TextView) view.findViewById(R.id.measure_text_view);
            quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);
        }

    }
    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredints_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientsAdapterViewHolder holder, int position) {
        Ingredients ingredients =mIngredients.get(position);
        ingredientTextView.setText(ingredients.getIngredient());
        measureTextView.setText(ingredients.getMeasure());
        quantityTextView.setText(String.valueOf(ingredients.getQuantity()));

    }

    @Override
    public int getItemCount() {
        if (null == mIngredients) return 0;
        return mIngredients.size();    }



    public void setIngredients(ArrayList<Ingredients> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }
}
