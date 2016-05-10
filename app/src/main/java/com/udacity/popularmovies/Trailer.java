package com.udacity.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.udacity.popularmovies.Utils.MovieUtils;

public class Trailer extends Fragment {

    RequestQueue requestQueue;
    ListView listView;
    private int movieid;

    @SuppressLint("ValidFragment")
    public Trailer(int movieid)
    {
        this.movieid=movieid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trailer, container, false);

        requestQueue= Volley.newRequestQueue(getContext());

        listView=(ListView)view.findViewById(R.id.listViewTrailer);
        getTrailerList();

        return view;
    }
    public void getTrailerList()
    {
        String TrailerURL = MovieUtils.BASE_MOVIE_URL+String.valueOf(movieid)+MovieUtils.Videos+MovieUtils.API_KEY;
        JsonObjectRequest jsonObjectRequest1= new JsonObjectRequest(Request.Method.GET, TrailerURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray trailerdetail=response.getJSONArray("results");
                    String[] TrailerName = new String[trailerdetail.length()];
                    final String[] TrailerKey = new String[trailerdetail.length()];
                    for(int i=0;i<trailerdetail.length();i++)
                    {
                        JSONObject trailersingledetail = trailerdetail.getJSONObject(i);
                        TrailerName[i] = trailersingledetail.getString("name");
                        TrailerKey[i] = trailersingledetail.getString("key");
                    }

                    SingleTrailer trailer=new SingleTrailer(getActivity(),TrailerName);
                    listView.setAdapter(trailer);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(MovieUtils.BASE_TRAILER_URL+TrailerKey[position])));
                        }
                    });

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
                error.printStackTrace();
                Toast.makeText(getActivity(),"BYE",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest1);
    }
}
