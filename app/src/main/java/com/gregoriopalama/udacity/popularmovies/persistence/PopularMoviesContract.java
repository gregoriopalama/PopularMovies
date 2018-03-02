package com.gregoriopalama.udacity.popularmovies.persistence;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for the Popular Movies Database and the Favorite Content Provider
 *
 * @author Gregorio Palamà
 */
public class PopularMoviesContract {
    private PopularMoviesContract() {
    }

    public static final String AUTHORITY = "com.gregoriopalama.udacity.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String FAVORITES_PATH = "favorites";

    public static class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVORITES_PATH).build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteaverage";
        public static final String COLUMN_NAME_VOTE_COUNT = "votecount";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static final String COLUMN_NAME_POSTER_PATH = "posterpath";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdroppath";
    }
}
