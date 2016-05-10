package com.udacity.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION= 1;
    public static final String DATABASE_NAME= "movie.db";

    final String CREATE_MOVIE_TABLE="CREATE TABLE " + MovieDBContract.MOVIE_ENTRY.TABLE_NAME + " ( "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_ID + " INTEGER PRIMARY AUTOINCREMENT,"
            + MovieDBContract.MOVIE_ENTRY.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_TITLE + " TEXT NOT NULL,"
            + MovieDBContract.MOVIE_ENTRY.COLUMN_USER_RATING + " INTEGER NOT NULL, "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_OVERVIEW + " TEXT NOT NULL, "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_POSTER + " BLOB, "
            + MovieDBContract.MOVIE_ENTRY.COLUMN_BACKDROP + " BLOB);";

    public MovieDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieDBContract.MOVIE_ENTRY.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
