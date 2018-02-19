package com.gregoriopalama.udacity.popularmovies.di;

import com.gregoriopalama.udacity.popularmovies.PopularMoviesApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Dagger component for the whole Application
 *
 * @author Gregorio Palamà
 */

@Singleton
@Component(modules = {AppModule.class,
        AndroidSupportInjectionModule.class,
        ActivitiesModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(PopularMoviesApplication application);
        AppComponent build();
    }
    void inject(PopularMoviesApplication application);
}
