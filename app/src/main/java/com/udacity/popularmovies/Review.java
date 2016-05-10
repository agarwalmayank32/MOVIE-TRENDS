package com.udacity.popularmovies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.popularmovies.Utils.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Review extends Fragment {

    RequestQueue requestQueue;
    private int movieid;
    String[] ReviewBy,Review;
    ListView listView;

    @SuppressLint("ValidFragment")
    public Review(int movieid)
    {
        this.movieid=movieid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, container, false);

        listView = (ListView)view.findViewById(R.id.listViewReview);

        requestQueue= Volley.newRequestQueue(getContext());

        getReviewList();

        return view;
    }

    public void getReviewList()
    {
        String ReviewURL= MovieUtils.BASE_MOVIE_URL+String.valueOf(movieid)+MovieUtils.Reviews+MovieUtils.API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ReviewURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Reviewdetail=response.getJSONArray("results");

                    if(Reviewdetail.length()<1)
                    {
                        Review = new String[1];
                        ReviewBy = new String[1];

                        ReviewBy[0] = "No reviews Present.";
                        Review[0] = "";
                    }
                    else
                    {
                        ReviewBy = new String[Reviewdetail.length()];
                        Review = new String[Reviewdetail.length()];
                        for(int i=0;i<Reviewdetail.length();i++)
                        {
                            JSONObject reviewsingledetail = Reviewdetail.getJSONObject(i);
                            ReviewBy[i] = "Reviewed By: "+reviewsingledetail.getString("author");
                            Review[i] = reviewsingledetail.getString("content");
                        }
                    }
                    SingleReview adapter = new SingleReview(getActivity(), Review, ReviewBy);
                    listView.setAdapter(adapter);
                }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity(),"BBYE",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"BYE",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
