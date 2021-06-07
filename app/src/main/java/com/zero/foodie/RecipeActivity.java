package com.zero.foodie;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zero.foodie.customAdapters.RecipePagerAdapter;
import com.zero.foodie.model.RecipeBrief;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    String url;
    RequestQueue mRequestQueue;
    ViewPager viewPager;
    ArrayList<RecipeBrief> recipeItem;
    RecipePagerAdapter recipeAdapter;
    SearchView recipeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        viewPager = findViewById(R.id.recipe_ViewPager);
        recipeSearch = findViewById(R.id.recipeSearchView);
        url = "https://api.spoonacular.com/food/search?apiKey=4a1db7c0abda4fbf8fcf5f693f281d18&number=20&query=";
        mRequestQueue = Volley.newRequestQueue(RecipeActivity.this);


        FillData(url);

recipeSearch.setOnCloseListener(new SearchView.OnCloseListener() {
    @Override
    public boolean onClose() {
        FillData(url);
//        notify();
        Toast.makeText(RecipeActivity.this, "ok", Toast.LENGTH_SHORT).show();
        return false;
    }
});
        recipeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FillData(url+query);
                Toast.makeText(RecipeActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }



    private void FillData(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // test.setText(response.toString());
                recipeItem = new ArrayList<>();
                try {
                    JSONArray searchResult = response.getJSONArray("searchResults");
                    JSONArray result = new JSONObject(searchResult.get(0).toString()).getJSONArray("results");
                    for (int i = 0; i < result.length(); i++) {
                        String name = result.getJSONObject(i).getString("name");
                        String imageurl = result.getJSONObject(i).getString("image");
                        String link = result.getJSONObject(i).getString("link");
                        String id = String.valueOf(result.getJSONObject(i).getInt("id"));
                        String briefIntro = result.getJSONObject(i).getString("content").replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
                        //   Toast.makeText(context, name+""+imageurl, Toast.LENGTH_SHORT).show();
                        recipeItem.add(new RecipeBrief(id, name, imageurl, briefIntro, link));
//                        recipeAdapter = new RecipeAdapter(context,recipeItem);
//                        recipeRecyclerView.setAdapter(recipeAdapter);
                        recipeAdapter = new RecipePagerAdapter(RecipeActivity.this, recipeItem);
                        viewPager.setAdapter(recipeAdapter);
                    }


                } catch (JSONException e) {
                    Toast.makeText(RecipeActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);
    }
}
