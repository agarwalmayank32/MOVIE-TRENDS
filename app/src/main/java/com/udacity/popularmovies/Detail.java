package com.udacity.popularmovies;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Detail extends Fragment {

    RequestQueue requestQueue,requestQueue1,requestQueue2;
    private int movieid;
    private String type;
    String[] ReviewBy, Review;
    String posterpath,BackdropPath;

    @InjectView(R.id.listViewReview)
    ListView listViewReview;
    @InjectView(R.id.listViewTrailer)
    ListView listViewTrailer;
    @InjectView(R.id.PosterPic)
    ImageView Poster;
    @InjectView(R.id.BackDrop)
    ImageView Backdrop;
    @InjectView(R.id.Title)
    TextView Title;
    @InjectView(R.id.UserRating)
    TextView UserRating;
    @InjectView(R.id.ReleaseDate)
    TextView ReleaseDate;
    @InjectView(R.id.OverView)
    TextView Overview;

    private boolean mFavorited;

    public Detail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            movieid = Integer.parseInt(bundle.getString("movieID"));
            type = bundle.getString("movieType");
        }

        ButterKnife.inject(this,view);

        requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue1= Volley.newRequestQueue(getActivity());
        requestQueue2= Volley.newRequestQueue(getActivity());

        setDetails(type);
        getReviewList();
        getTrailerList();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavorited=isFavourite();
        final FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(toggleFavoriteButton());
            }
        });
        if(mFavorited){
            fab.setImageResource(R.drawable.ic_favorite_selected);
        }
        else{
            fab.setImageResource(R.drawable.ic_favorite_not_selected);
        }

    }

    public void setDetails(String type1)
    {
        String detailURL = MovieUtils.BASE_MOVIE_URL+type1+MovieUtils.API_KEY;

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
                            posterpath=MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE1+moviesingledetail.getString("poster_path");
                            BackdropPath=MovieUtils.BASE_PICTURE_URL+MovieUtils.PICTURE_SIZE2+moviesingledetail.getString("backdrop_path");
                            Picasso.with(getActivity()).load(posterpath).into(Poster);
                            Picasso.with(getActivity()).load(BackdropPath).into(Backdrop);
                        }
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(getActivity(),"BBBBBBBBBBBBBBBBYE",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"BBBBBYE from detail",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
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
                    listViewTrailer.setAdapter(trailer);


                    listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        requestQueue1.add(jsonObjectRequest1);
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
                    listViewReview.setAdapter(adapter);
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

        requestQueue2.add(jsonObjectRequest);
    }

    public boolean isFavourite()
    {
        int flag=0;
        Cursor cursor = getActivity().getContentResolver().query(MovieDBContentProvider.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MovieDBContract.MOVIE_ENTRY.COLUMN_MOVIE_ID)));
            if(mid==movieid)
            {
                flag=1;
                break;
            }
        }
        if(flag==1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Uri savefavorite()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_MOVIE_ID,movieid);
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_POSTER,posterpath);
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_BACKDROP,BackdropPath);
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_TITLE,Title.getText().toString());
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_OVERVIEW,Overview.getText().toString());
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_RELEASE_DATE,ReleaseDate.getText().toString());
        contentValues.put(MovieDBContract.MOVIE_ENTRY.COLUMN_USER_RATING,UserRating.getText().toString());
        return getActivity().getContentResolver().insert(MovieDBContentProvider.CONTENT_URI,contentValues);
    }

    public int deletefavorite()
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        String selection = MovieDBContract.MOVIE_ENTRY.COLUMN_MOVIE_ID + " = \"" + String.valueOf(movieid) + "\"";
        return contentResolver.delete(MovieDBContentProvider.CONTENT_URI,selection,null);
    }

    public int toggleFavoriteButton()
    {
        mFavorited = !mFavorited;
        if(!mFavorited){
            deletefavorite();
            Toast.makeText(getActivity(),"Movie Unfavorited",Toast.LENGTH_SHORT).show();
            return R.drawable.ic_favorite_not_selected;
        }
        else{
            savefavorite();
            Toast.makeText(getActivity(),"Movie Favorited",Toast.LENGTH_SHORT).show();
            return  R.drawable.ic_favorite_selected;
        }
    }

}
