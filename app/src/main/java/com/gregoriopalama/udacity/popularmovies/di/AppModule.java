package com.gregoriopalama.udacity.popularmovies.di;

import android.app.Application;

import com.gregoriopalama.udacity.popularmovies.PopularMoviesApplication;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;

/**
 * Dagger module that provides the context
 *
 * @author Gregorio Palamà
 */

@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    @Provides
    Application provideContext(PopularMoviesApplication application) {
        return application;
    }
}
