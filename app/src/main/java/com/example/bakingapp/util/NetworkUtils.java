package com.example.bakingapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.IdlingResource;

import com.example.bakingapp.SimpleIdlingResource;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Steps;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String MIME_VIDEO = "video/";
    public static final String MIME_IMAGE = "image/";
    public static final String PREFERENCES_WIDGET_TITLE = "WIDGET_TITLE";
    public static final String PREFERENCES_WIDGET_CONTENT = "WIDGET_CONTENT";
    public static final String PREFERENCES_ID = "ID";

    public static ArrayList<Recipe> fetchRecipeData(String url) throws IOException {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        try {

            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseRecipeJson(results,recipes);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }


    public static void parseRecipeJson(String data, ArrayList<Recipe> list){


        try {
            JSONArray mainObject = new JSONArray(data);
            Log.v(TAG,data);
            for (int i = 0; i < mainObject.length(); i++) {
                ArrayList<Ingredients> ingredient = new ArrayList<>();
                ArrayList<Steps> step =  new ArrayList<>();
                JSONObject jsonObject = mainObject.getJSONObject(i);
                Recipe recipe = new Recipe();
                recipe.setId(jsonObject.getString("id"));
                recipe.setName(jsonObject.getString("name"));
                JSONArray jsonArrayIngredients = jsonObject.getJSONArray("ingredients");

                for (int j=0;j<jsonArrayIngredients.length();j++){
                    JSONObject jsonIngredientsObject = jsonArrayIngredients.getJSONObject(j);
                    Ingredients ingredients=new Ingredients();
                    ingredients.setQuantity(jsonIngredientsObject.getInt("quantity"));
                    ingredients.setMeasure(jsonIngredientsObject.getString("measure"));
                    ingredients.setIngredient(jsonIngredientsObject.getString("ingredient"));
                    ingredient.add(ingredients);
                }

                recipe.setIngredients(ingredient);
                JSONArray jsonArraySteps = jsonObject.getJSONArray("steps");

                for (int k=0;k<jsonArraySteps.length();k++){
                    JSONObject jsonStepsObject = jsonArraySteps.getJSONObject(k);
                    Steps steps=new Steps();
                    steps.setId(jsonStepsObject.getInt("id"));
                    steps.setShortDescription(jsonStepsObject.getString("shortDescription"));
                    steps.setDescription(jsonStepsObject.getString("description"));
                    steps.setVideoURL(jsonStepsObject.getString("videoURL"));
                    steps.setThumbnailURL(jsonStepsObject.getString("thumbnailURL"));
                    step.add(steps);
                }

                recipe.setSteps(step);
                recipe.setServings(jsonObject.getString("servings"));
                recipe.setImage(jsonObject.getString("image"));
                list.add(recipe);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Erro occurred during JSON Parsing");
        }

    }



    public static Boolean networkStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }
    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    @Nullable
    private static SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public static IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
