package com.gregoriopalama.udacity.popularmovies.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.api.MovieDbApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides Retrofit and the MovieDb API consumer
 *
 * @author Gregorio Palamà
 */

@Module
public class MovieDBApiModule {

    @Singleton
    @Provides
    Gson providesGson() {
        return  new GsonBuilder()
                .setDateFormat(Constants.MOVIEDB_API_DATE_FORMAT)
                .create();
    }

    @Singleton
    @Provides
    Retrofit providesRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.MOVIEDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory
                        .createWithScheduler(Schedulers.io()))
                .build();
    }

    @Singleton
    @Provides
    MovieDbApi providesMovieDbApi(Retrofit retrofit) {
        return retrofit.create(MovieDbApi.class);
    }
}
