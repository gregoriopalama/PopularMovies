package com.gregoriopalama.udacity.popularmovies.api;

import com.gregoriopalama.udacity.popularmovies.api.dto.QueryResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Maps the MovieDb API on /movi/popular and /movie/top_rated endpoints
 *
 * @author Gregorio Palamà
 */

public interface MovieDbApi {

    @GET("movie/popular")
    Observable<QueryResult> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<QueryResult> getTopRatedMovies(@Query("api_key") String apiKey);

}
