package com.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    static String BASE_URL="http://api.themoviedb.org/3/movie/";
    static String api_key="?api_key=";
    static String BASE_MOVIE_URL="http://image.tmdb.org/t/p/w185/";

    String[] movieposters;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView=(GridView)findViewById(R.id.gridView);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        MovieDetailReceive("popular");

    }

    public void MovieDetailReceive(final String parturl)
    {

        String mainurl=BASE_URL+parturl+api_key;

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, mainurl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray moviedetail=response.getJSONArray("results");
                    movieposters= new String[moviedetail.length()];
                    for(int i=0;i<moviedetail.length();i++)
                    {
                        movieposters[i]=BASE_MOVIE_URL+moviedetail.getJSONObject(i).getString("poster_path");
                    }

                    Movie_List_Implement adapter = new Movie_List_Implement(MainActivity.this,movieposters,moviedetail.length());
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent= new Intent(MainActivity.this,MovieDetail.class);
                            intent.putExtra("Id",String.valueOf(i));
                            intent.putExtra("Kind",parturl);
                            startActivity(intent);
                        }
                    });


                }
                catch (JSONException e)
                {
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"Internet Not Available",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Popular)
        {

            MovieDetailReceive("popular");
            return true;
        }
        else if(id== R.id.TopRated)
        {
            MovieDetailReceive("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
