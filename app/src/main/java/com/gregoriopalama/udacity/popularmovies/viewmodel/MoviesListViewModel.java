package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;

import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.api.MovieDbApi;
import com.gregoriopalama.udacity.popularmovies.api.dto.Movie;
import com.gregoriopalama.udacity.popularmovies.api.dto.QueryResult;
import com.gregoriopalama.udacity.popularmovies.ui.detail.DetailActivity;
import com.gregoriopalama.udacity.popularmovies.ui.list.ItemMovieListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel that holds informations on the movies' list
 *
 * @author Gregorio Palamà
 */
public class MoviesListViewModel extends ViewModel implements ItemMovieListener {
    public static final int SORT_ORDER_POPULAR = 0;
    public static final int SORT_ORDER_TOP_RATED = 1;

    private MutableLiveData<List<Movie>> movies;
    private MovieDbApi movieDbService;
    private Application application;
    private MutableLiveData<Integer> sortOrder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MoviesListViewModel(MovieDbApi movieDbService, Application application) {
        this.movieDbService = movieDbService;
        this.application = application;
        movies = new MutableLiveData<>();
        sortOrder = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder.setValue(sortOrder);
    }

    public MutableLiveData<Integer> getSortOrder() {
        return sortOrder;
    }

    public void retrievePopularMovies() {
        retrieveMovies(movieDbService.getPopularMovies(Constants.MOVIEDB_API_KEY));
    }

    public void retrieveTopRatedMovies() {
        retrieveMovies(movieDbService.getTopRatedMovies(Constants.MOVIEDB_API_KEY));
    }

    private void retrieveMovies(Observable<QueryResult> observable) {
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<QueryResult>() {
                    @Override
                    public void onNext(QueryResult queryResult) {
                        if (queryResult.getResults().size() == 0 )
                            return;

                        movies.setValue(new ArrayList<>());

                        for (Movie movie : queryResult.getResults()) {
                            movies.getValue().add(movie);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
        }));
    }

    @Override
    public void openDetail(Movie movie) {
        Intent intent = new Intent(application.getApplicationContext(), DetailActivity.class);
        intent.putExtra(Constants.DETAIL_MOVIE_EXTRA_NAME, movie);
        application.startActivity(intent);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }
}
