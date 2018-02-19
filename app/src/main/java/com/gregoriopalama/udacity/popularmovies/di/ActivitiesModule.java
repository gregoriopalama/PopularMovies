package com.gregoriopalama.udacity.popularmovies.di;

import com.gregoriopalama.udacity.popularmovies.ui.detail.DetailActivity;
import com.gregoriopalama.udacity.popularmovies.ui.list.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger module for Activities
 *
 * @author Gregorio Palamà
 */

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity bindDetailActivity();
}
