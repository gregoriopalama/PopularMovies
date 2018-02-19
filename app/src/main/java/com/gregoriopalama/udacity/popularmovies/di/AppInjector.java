package com.gregoriopalama.udacity.popularmovies.di;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.gregoriopalama.udacity.popularmovies.PopularMoviesApplication;

import dagger.android.AndroidInjection;

/**
 * AppInjector that injects the Application and register a callback that listens
 * to all the activities' lifecycles. It injects the activities when they are created
 *
 * @author Gregorio Palamà
 */

public class AppInjector {
    private AppInjector() {
    }

    public static void init(PopularMoviesApplication application) {
        DaggerAppComponent.builder().application(application)
                .build().inject(application);
        application
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        AndroidInjection.inject(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }
}
