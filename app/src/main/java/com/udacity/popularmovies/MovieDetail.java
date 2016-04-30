package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MovieDetail extends AppCompatActivity {

    private static int movieid;
    private static String moviekind;
    private RequestQueue requestQueue;

    ImageView Poster,Backdrop;
    TextView Overview,UserRating,ReleaseDate,Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Title=(TextView)findViewById(R.id.TitleText);
        UserRating=(TextView)findViewById(R.id.UserRatingText);
        ReleaseDate=(TextView)findViewById(R.id.RelaseDateText);
        Overview=(TextView)findViewById(R.id.OverviewText);
        Poster=(ImageView)findViewById(R.id.PosterPic);
        Backdrop=(ImageView)findViewById(R.id.BackDrop);

        requestQueue = Volley.newRequestQueue(MovieDetail.this);

        Bundle extra= getIntent().getExtras();
        if(extra!=null)
        {
            movieid=Integer.parseInt(extra.getString("Id"));
            moviekind=extra.getString("Kind");
            setDetails(moviekind,movieid);
        }
    }

    public void setDetails(String parturl,final int id)
    {
        String mainurl=MainActivity.BASE_URL+parturl+MainActivity.api_key;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, mainurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray moviedetail=response.getJSONArray("results");
                    JSONObject moviesingledetail=moviedetail.getJSONObject(id);

                    Title.setText(moviesingledetail.getString("original_title"));
                    UserRating.setText(moviesingledetail.getString("vote_average"));
                    ReleaseDate.setText(moviesingledetail.getString("release_date"));
                    Overview.setText(moviesingledetail.getString("overview"));
                    Picasso.with(MovieDetail.this).load(MainActivity.BASE_MOVIE_URL+moviesingledetail.getString("poster_path")).into(Poster);

                    Picasso.with(MovieDetail.this).load("http://image.tmdb.org/t/p/w500/"+moviesingledetail.getString("backdrop_path")).into(Backdrop);
                }
                catch (JSONException e)
                {
                    Toast.makeText(MovieDetail.this,"BBYE",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MovieDetail.this,"BYE",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}