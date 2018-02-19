package com.gregoriopalama.udacity.popularmovies.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.gregoriopalama.udacity.popularmovies.viewmodel.MovieViewModel;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Dagger module that provides all the ViewModels
 *
 * @author Gregorio Palamà
 */

@Module(includes = {MovieDBApiModule.class})
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesListViewModel.class)
    abstract ViewModel bindMoviesListViewModel(MoviesListViewModel moviesListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel.class)
    abstract ViewModel bindMovieViewModel(MovieViewModel movieViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
