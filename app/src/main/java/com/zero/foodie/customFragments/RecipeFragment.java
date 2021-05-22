package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zero.foodie.R;
import com.zero.foodie.model.RecipeBrief;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class RecipeFragment extends Fragment {
    private Context context;
    String url;
    RequestQueue mRequestQueue;
    TextView test;
    EditText searchEditView;
    Button recipeSearch;
    LinearLayout recipeSearchLayout;
    List<RecipeBrief> recipeItem;
    String test1;


    public RecipeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        test1 = "";
        url = "https://api.spoonacular.com/food/search?apiKey=4a1db7c0abda4fbf8fcf5f693f281d18&number=20&query=";
        View v = inflater.inflate(R.layout.fragment_recipe, parent, false);
        searchEditView = v.findViewById(R.id.recipeSearchEditTxt);
        recipeSearch =  v.findViewById(R.id.recipeSearchButton);
        recipeItem = new ArrayList<>();
        test = v.findViewById(R.id.test);
        recipeSearchLayout = v.findViewById(R.id.recipeSearchLayout);
        recipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeSearchLayout.setVisibility(GONE);
                url = url+searchEditView.getText().toString().trim();
                FillData(url);
              }
        });

        mRequestQueue = Volley.newRequestQueue(context);


        return v;
    }

    private void FillData(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // test.setText(response.toString());
                try {
                    JSONArray searchResult = response.getJSONArray("searchResults");
                    JSONArray result = new JSONObject(searchResult.get(0).toString()).getJSONArray("results");
                    for(int i=0;i<result.length();i++) {
                        String name = result.getJSONObject(i).getString("name");
                        String imageurl = result.getJSONObject(i).getString("image");
                        String link = result.getJSONObject(i).getString("link");
                        String id = String.valueOf(result.getJSONObject(i).getInt("id"));
                        String briefIntro = result.getJSONObject(i).getString("content").replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
                     //   Toast.makeText(context, name+""+imageurl, Toast.LENGTH_SHORT).show();
                        recipeItem.add(new RecipeBrief(id,name,imageurl,briefIntro,link));
                    }

                    for(int i= 0;i<recipeItem.size();i++)
                    {
                        test1 += recipeItem.get(i).getName()+"\n"+recipeItem.get(i).getLink()+"\n\n";
                    }
                    test.setText(test1);
                } catch (JSONException e) {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);
    }
}
