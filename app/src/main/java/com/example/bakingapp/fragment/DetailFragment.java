package com.example.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.IngredientsAdapter;
import com.example.bakingapp.adapter.StepsAdapter;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Steps;

import java.util.ArrayList;

public class DetailFragment extends Fragment {

    public LinearLayoutManager layoutManagerIngredints;
    public LinearLayoutManager layoutManagerSteps;
    private Parcelable savedIngredintsRecyclerLayoutState;
    private Parcelable savedStepsRecyclerLayoutState;

    final String  KEY_INSTANCE_STATE_RV_POSITION_STEPS="save step";
    final String  KEY_INSTANCE_STATE_RV_POSITION_INGREDIENTS="save ingredients";
    View rootView;



    // Tag for logging
    private static final String TAG = "DetailFragment";

    private ArrayList<Ingredients> ingredients;
    ArrayList<Steps> steps;
    StepsAdapter sAdapter;
    RecyclerView ingredientRecyclerView;
    RecyclerView stepRecyclerView;
    Context context;

    public void setDetailFragment(ArrayList<Ingredients> mIngredients,ArrayList<Steps> mSteps,Context context) {
        this.ingredients = mIngredients;
        this.steps=mSteps;
        this.context=context;
    }
public DetailFragment(){}

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected(Steps step);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTextClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ingredientRecyclerView =  rootView.findViewById(R.id.recycler_view_ingredints);

        stepRecyclerView =  rootView.findViewById(R.id.recycler_view_steps);



        // save the currently selected tab, so we can restore it in case of a phone rotation
        if (savedInstanceState != null) {
            layoutManagerIngredints.onRestoreInstanceState(savedIngredintsRecyclerLayoutState);
            layoutManagerSteps.onRestoreInstanceState(savedStepsRecyclerLayoutState);
        }else{
            layoutManagerSteps=new LinearLayoutManager(context);
            layoutManagerIngredints=new LinearLayoutManager(context);
        }


       layoutManagerIngredints.setStackFromEnd(false);


        layoutManagerSteps.setStackFromEnd(false);

        ingredientRecyclerView.setLayoutManager(layoutManagerIngredints);
        ingredientRecyclerView.setHasFixedSize(true);
        IngredientsAdapter mAdapter = new IngredientsAdapter();
        mAdapter.setIngredients(ingredients);
        ingredientRecyclerView.setAdapter(mAdapter);

        stepRecyclerView.setLayoutManager(layoutManagerSteps);
        stepRecyclerView.setHasFixedSize(true);
        sAdapter = new StepsAdapter(steps, new StepsAdapter.StepItemClickListener() {
            @Override
            public void onStepItemClick(Steps steps) {
                mCallback.onStepSelected(steps);
            }
        });
        stepRecyclerView.setAdapter(sAdapter);



        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            savedIngredintsRecyclerLayoutState=savedInstanceState.getParcelable(KEY_INSTANCE_STATE_RV_POSITION_INGREDIENTS);
            savedStepsRecyclerLayoutState=savedInstanceState.getParcelable(KEY_INSTANCE_STATE_RV_POSITION_STEPS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION_INGREDIENTS,layoutManagerIngredints.onSaveInstanceState());
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION_STEPS,layoutManagerSteps.onSaveInstanceState());

    }

    }


