package com.gregoriopalama.udacity.popularmovies.api;

import com.gregoriopalama.udacity.popularmovies.model.MovieReviews;
import com.gregoriopalama.udacity.popularmovies.model.MovieVideos;
import com.gregoriopalama.udacity.popularmovies.model.QueryResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Maps the MovieDb API on /movie/popular, /movie/top_rated,
 * movie/{id}/videos and movie/{id}/reviews endpoints
 *
 * @author Gregorio Palamà
 */

public interface MovieDbApi {

    @GET("movie/popular")
    Observable<QueryResult> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<QueryResult> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Observable<MovieVideos> getMovieVideos(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<MovieReviews> getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey);
}
