package com.udacity.popularmovies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Utils.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Detail extends Fragment {

    RequestQueue requestQueue;
    private int movieid;
    private String type;

    private ImageView Poster,Backdrop;
    private TextView Title,UserRating,ReleaseDate,Overview;

    @SuppressLint("ValidFragment")
    public Detail(int movieid,String type)
    {
        this.movieid=movieid;
        this.type=type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);

        requestQueue= Volley.newRequestQueue(getContext());
        Title=(TextView)view.findViewById(R.id.Title);
        UserRating=(TextView)view.findViewById(R.id.UserRating);
        ReleaseDate=(TextView)view.findViewById(R.id.ReleaseDate);
        Overview=(TextView)view.findViewById(R.id.OverView);
        Poster=(ImageView)view.findViewById(R.id.PosterPic);
        Backdrop=(ImageView)view.findViewById(R.id.BackDrop);

        setDetails();

        return view;
    }

    public void setDetails()
    {
        String detailURL = MovieUtils.BASE_MOVIE_URL+type+MovieUtils.API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, detailURL, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray moviedetail=response.getJSONArray("results");
                    for(int i=0;i<moviedetail.length();i++)
                    {
                        JSONObject moviesingledetail=moviedetail.getJSONObject(i);
                        if(moviesingledetail.getString("id").equals(String.valueOf(movieid)))
                        {
                            Title.setText(moviesingledetail.getString("original_title"));
                            UserRating.setText("User Rating: "+moviesingledetail.getString("vote_average"));
                            ReleaseDate.setText("Release Date:"+moviesingledetail.getString("release_date"));
                            Overview.setText(moviesingledetail.getString("overview"));

                            Picasso.with(getActivity()).load(MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE1+moviesingledetail.getString("poster_path")).into(Poster);
                            Picasso.with(getActivity()).load(MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE2+moviesingledetail.getString("backdrop_path")).into(Backdrop);
                        }
                    }
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
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
