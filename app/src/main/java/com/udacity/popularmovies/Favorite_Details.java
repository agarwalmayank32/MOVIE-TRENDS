package com.udacity.popularmovies;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Favorite_Details extends Fragment {

    String Poster,BackDrop;
    @InjectView(R.id.favTitle)
    TextView favTitle;
    @InjectView(R.id.favUserRating)
    TextView favUser_Rating;
    @InjectView(R.id.favReleaseDate)
    TextView favRelease_Date;
    @InjectView(R.id.favOverView)
    TextView favOverview;
    @InjectView(R.id.favPosterPic)
    ImageView favPoster;
    @InjectView(R.id.favBackDrop)
    ImageView favBackDrop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favorite_details, container, false);

        ButterKnife.inject(this,view);

        Bundle bundle = getArguments();
        if(bundle != null){
            String movieID = bundle.getString("movieID");
            setfavMovieDetails(movieID);
        }
        return view;
    }

    public void setfavMovieDetails(String movieID)
    {

        Cursor cursor = getActivity().getContentResolver().query(MovieDBContentProvider.CONTENT_URI, null, null, null, null);

        if (!cursor.moveToFirst()) {
            Toast.makeText(getActivity(), " no record found yet!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            do
            {
                int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_MOVIE_ID)));
                if(mid==Integer.parseInt(movieID))
                {
                    favTitle.setText(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_TITLE)));
                    Poster = cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_POSTER));
                    BackDrop = cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_BACKDROP));
                    favUser_Rating.setText(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_USER_RATING)));
                    favRelease_Date.setText(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_RELEASE_DATE)));
                    favOverview.setText(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_OVERVIEW)));

                    Picasso.with(getActivity()).load(Poster).into(favPoster);
                    Picasso.with(getActivity()).load(BackDrop).into(favBackDrop);
                    break;
                }
            }while (cursor.moveToNext());
        }
    }
}
