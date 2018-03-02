package com.gregoriopalama.udacity.popularmovies.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Popular Movies DB Helper
 *
 * @author Gregorio Palamà
 */
public class PopularMoviesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PopularMoviesContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                    PopularMoviesContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_RELEASE_DATE + " INTEGER," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT," +
                    PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PopularMoviesContract.FavoriteMovieEntry.TABLE_NAME;

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
