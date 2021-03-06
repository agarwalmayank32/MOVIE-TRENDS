package com.udacity.popularmovies;

import android.provider.BaseColumns;

public class MovieDBContract {

    public static final class MOVIE_ENTRY implements BaseColumns {

        public static final String TABLE_NAME="movie";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_MOVIE_ID="MOVIE_ID";
        public static final String COLUMN_POSTER="POSTER";
        public static final String COLUMN_BACKDROP="BACKDROP";
        public static final String COLUMN_TITLE="TITLE";
        public static final String COLUMN_USER_RATING="USER_RATING";
        public static final String COLUMN_RELEASE_DATE="RELEASE_DATE";
        public static final String COLUMN_OVERVIEW="OVERVIEW";
    }
}
