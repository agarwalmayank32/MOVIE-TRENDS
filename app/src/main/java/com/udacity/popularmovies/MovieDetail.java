package com.udacity.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import com.udacity.popularmovies.Utils.MovieUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class MovieDetail extends AppCompatActivity {

    private static int movieid;
    private static String moviekind;

    FloatingActionButton fab;

    private ImageView Poster,Backdrop;
    private TextView Title,UserRating,ReleaseDate,Overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra= getIntent().getExtras();
        if(extra!=null)
        {
            movieid=Integer.parseInt(extra.getString("Id"));
            moviekind=extra.getString("Kind");
            setDetails(moviekind,movieid);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Title=(TextView)findViewById(R.id.Title);
        UserRating=(TextView)findViewById(R.id.UserRating);
        ReleaseDate=(TextView)findViewById(R.id.ReleaseDate);
        Overview=(TextView)findViewById(R.id.OverView);
        Poster=(ImageView)findViewById(R.id.PosterPic);
        Backdrop=(ImageView)findViewById(R.id.BackDrop);


        MovieUtils.requestQueue = Volley.newRequestQueue(MovieDetail.this);
    }

    public void setDetails(String parturl,final int id)
    {
        String mainurl= MovieUtils.BASE_MOVIE_URL+parturl+MovieUtils.API_KEY;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, mainurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray moviedetail=response.getJSONArray("results");
                    JSONObject moviesingledetail=moviedetail.getJSONObject(id);

                    String movieid=moviesingledetail.getString("id");
                    Toast.makeText(MovieDetail.this,movieid,Toast.LENGTH_SHORT).show();
                    Title.setText(moviesingledetail.getString("original_title"));
                    UserRating.setText("User Rating: "+moviesingledetail.getString("vote_average"));
                    ReleaseDate.setText("Release Date:"+moviesingledetail.getString("release_date"));
                    Overview.setText(moviesingledetail.getString("overview"));
                    
                    Picasso.with(MovieDetail.this).load(MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE1+moviesingledetail.getString("poster_path")).into(Poster);
                    Picasso.with(MovieDetail.this).load(MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE2+moviesingledetail.getString("backdrop_path")).into(Backdrop);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MovieDetail.this,"Movie favourited",Toast.LENGTH_SHORT).show();

                        }
                    });

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

        MovieUtils.requestQueue.add(jsonObjectRequest);
    }

}
