package com.udacity.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Movie_Poster extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__poster);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Movie_PosterFragment fragment =new Movie_PosterFragment();
        if (id == R.id.Popular)
        {
            fragment.MovieDetailReceive("popular");
            return true;
        }
        else if(id== R.id.TopRated)
        {
            fragment.MovieDetailReceive("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
