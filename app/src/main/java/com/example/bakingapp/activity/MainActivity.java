package com.example.bakingapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapter.MainAdapter;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.util.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.RecipeAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MainAdapter mMainAdapter;
    ArrayList<Recipe> mRecipes;

    final static String START_DETAIL="Start Detail";



    final String JSON_URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    final String  KEY_INSTANCE_STATE_RV_POSITION="save";
    private Parcelable savedRecyclerLayoutState;

    public LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView =  findViewById(R.id.recyclerview_recipe);
        if(savedRecyclerLayoutState!=null){

            layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);

        }else{

            layoutManager
                    = new LinearLayoutManager(this);
        }
        layoutManager.setStackFromEnd(false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mMainAdapter = new MainAdapter( this,mRecipes);


        mRecyclerView.setAdapter(mMainAdapter);



        if(NetworkUtils.networkStatus(MainActivity.this)){


            new FetchRecipeTask().execute();
        }else{
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION, layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState=savedInstanceState.getParcelable(KEY_INSTANCE_STATE_RV_POSITION);
        }
    }

    public class FetchRecipeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                mRecipes= NetworkUtils.fetchRecipeData(JSON_URL);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mRecipes!=null){
                try{
                    mMainAdapter.setRecipe(mRecipes);
                }catch (Exception e){
                    e.getStackTrace();
                }}

        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent=new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(START_DETAIL,recipe);
        startActivity(intent);

    }

}

