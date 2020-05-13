package com.example.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecipeAdapterViewHolder> {
    TextView recipeTextView;

    private  final  RecipeAdapterOnClickHandler mClickHandler;


    private ArrayList<Recipe> mRecipe;

    public MainAdapter(RecipeAdapterOnClickHandler clickHandler,ArrayList<Recipe> recipe){
        mClickHandler =  clickHandler;
        mRecipe =recipe;
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public RecipeAdapterViewHolder(View view) {
            super(view);
            recipeTextView = (TextView) view.findViewById(R.id.recipe_text_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe =mRecipe.get(getAdapterPosition());
            mClickHandler.onClick(recipe);

        }


    }


    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.RecipeAdapterViewHolder holder, int position) {

        Recipe recipe =mRecipe.get(position);
        recipeTextView.setText(recipe.getName());

    }


    @Override
    public int getItemCount() {
        if (null == mRecipe) return 0;
        return mRecipe.size();
    }

    public void setRecipe(ArrayList<Recipe> recipes) {
        mRecipe = recipes;
        notifyDataSetChanged();
    }

}