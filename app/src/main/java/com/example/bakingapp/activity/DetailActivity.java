package com.example.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.bakingapp.BakingWidgetProvider;
import com.example.bakingapp.BuildConfig;
import com.example.bakingapp.R;
import com.example.bakingapp.fragment.DetailFragment;
import com.example.bakingapp.fragment.StepDetailFragment;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.util.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnStepClickListener {

    ArrayList<Ingredients> ingredients;
    ArrayList<Steps> steps;
    FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;
    LinearLayout layoutRecipeDetail;

    private boolean mTwoPane;
    Recipe recipe;

    final static String START_DETAIL_STEP="Start Detail Activity";
    final static String START_DETAIL_STEPS="Start Detail STEPS Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);
        // Change the GridView to space out the images more on tablet

        layoutRecipeDetail=findViewById(R.id.layout_recipe_detail);



        Intent intent = getIntent();
        recipe = intent.getExtras().getParcelable(MainActivity.START_DETAIL);
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();
        mTwoPane = (findViewById(R.id.fragment_container_detail) != null);

        if (savedInstanceState == null) {

            // In two-pane mode, add initial BodyPartFragments to the screen

            fragmentManager = getSupportFragmentManager();

            // Creating a new head fragment
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setDetailFragment(ingredients,steps,this);
            fragmentManager.beginTransaction()
                    .add(R.id.master_list_fragment, detailFragment)
                    .commit();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // persistence.  Set checked state based on the fetchPopular boolean
        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if ((sharedPreferences.getInt("ID", -1) == Integer.parseInt(recipe.getId()))){
            menu.findItem(R.id.mi_action_widget);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mi_action_widget){
            boolean isRecipeInWidget = (sharedPreferences.getInt(NetworkUtils.PREFERENCES_ID, -1) == Integer.parseInt(recipe.getId()));

            // If recipe already in widget, remove it
            if (isRecipeInWidget){
                sharedPreferences.edit()
                        .remove(NetworkUtils.PREFERENCES_ID)
                        .remove(NetworkUtils.PREFERENCES_WIDGET_TITLE)
                        .remove(NetworkUtils.PREFERENCES_WIDGET_CONTENT)
                        .apply();

                Snackbar.make(layoutRecipeDetail, R.string.widget_recipe_removed, Snackbar.LENGTH_SHORT).show();
            }
            // if recipe not in widget, then add it
            else{
                sharedPreferences
                        .edit()
                        .putInt(NetworkUtils.PREFERENCES_ID, Integer.parseInt(recipe.getId()))
                        .putString(NetworkUtils.PREFERENCES_WIDGET_TITLE, recipe.getName())
                        .putString(NetworkUtils.PREFERENCES_WIDGET_CONTENT, ingredientsString())
                        .apply();

                Snackbar.make(layoutRecipeDetail, R.string.widget_recipe_added, Snackbar.LENGTH_SHORT).show();
            }

            // Put changes on the Widget
            ComponentName provider = new ComponentName(this, BakingWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] ids = appWidgetManager.getAppWidgetIds(provider);
            BakingWidgetProvider bakingWidgetProvider = new BakingWidgetProvider();
            bakingWidgetProvider.onUpdate(this, appWidgetManager, ids);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepSelected(Steps step) {
        if (!step.getThumbnailURL().isEmpty()){
            String mimeType = NetworkUtils.getMimeType(this, Uri.parse(step.getThumbnailURL()));
            if (mimeType.startsWith(NetworkUtils.MIME_VIDEO)){
                step.swapVideoWithThumb();
            }
        }
        if (!step.getVideoURL().isEmpty()){
            String mimeType = NetworkUtils.getMimeType(this, Uri.parse(step.getVideoURL()));
            if (mimeType.startsWith(NetworkUtils.MIME_IMAGE)){
                step.swapVideoWithThumb();
            }
        }if (mTwoPane){
            StepDetailFragment detailFragment = new StepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(START_DETAIL_STEP, step);
            detailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_detail, detailFragment)
                    .commit();
        }else {
            Intent intent = new Intent(DetailActivity.this, StepDetailActivity.class);
            intent.putExtra(START_DETAIL_STEP, step);
            intent.putParcelableArrayListExtra(START_DETAIL_STEPS, steps);
            // intent.putExtra(AppUtils.EXTRAS_RECIPE_NAME, recipe.getName());
            startActivity(intent);
        }
    }
    private String ingredientsString(){
        StringBuilder result = new StringBuilder();
        for (Ingredients ingredient :  recipe.getIngredients()){
            result.append(ingredient.getDoseStr()).append(" ").append(ingredient.getIngredient()).append("\n");
        }
        return result.toString();
    }
}
