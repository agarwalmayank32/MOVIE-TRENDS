package com.udacity.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SingleTrailer extends ArrayAdapter<String> {

    String[] TrailerTitle;
    Activity context;
    TextView textView;

    public SingleTrailer(Activity context, String[] TrailerTitle) {
        super(context,R.layout.singletrailer,TrailerTitle);
        this.context=context;
        this.TrailerTitle=TrailerTitle;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.singletrailer, null, true);

        textView=(TextView)rowView.findViewById(R.id.TrailerTextView);
        textView.setText(TrailerTitle[position]);

        return rowView;
    }
}
