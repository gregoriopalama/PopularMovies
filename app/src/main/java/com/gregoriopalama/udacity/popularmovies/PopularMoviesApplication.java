package com.gregoriopalama.udacity.popularmovies;

import android.app.Activity;
import android.app.Application;

import com.gregoriopalama.udacity.popularmovies.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * The Application. It injects itself when it is created using the AppInjector
 *
 * @see AppInjector
 * @author Gregorio Palamà
 */

public class PopularMoviesApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
