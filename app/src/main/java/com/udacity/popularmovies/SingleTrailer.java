package com.udacity.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/**
 * Created by mayank on 6/5/16.
 */
public class SingleTrailer extends ArrayAdapter {

    String[] TrailerTitle;

    public SingleTrailer(Context context, String[] TrailerTitle) {

        super(context,R.layout.singletrailer,TrailerTitle);
        this.TrailerTitle=TrailerTitle;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {



        return null;
    }
}
