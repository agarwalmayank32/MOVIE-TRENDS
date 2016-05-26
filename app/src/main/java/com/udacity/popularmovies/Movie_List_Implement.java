package com.udacity.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class Movie_List_Implement extends ArrayAdapter<String> {

    private final Activity activity;
    private final String[] MoviePoster;

    Movie_List_Implement(Activity activity,String[] MoviePoster)
    {
        super(activity,R.layout.singleposter,MoviePoster);
        this.activity=activity;
        this.MoviePoster=MoviePoster;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = activity.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.singleposter, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.singlePoster);
        Picasso.with(activity).load(MoviePoster[i]).into(imageView);
        return rowView;
    }
}
