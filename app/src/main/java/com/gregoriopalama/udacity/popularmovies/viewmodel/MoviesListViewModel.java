package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.database.Cursor;

import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.api.MovieDbApi;
import com.gregoriopalama.udacity.popularmovies.model.Movie;
import com.gregoriopalama.udacity.popularmovies.model.QueryResult;
import com.gregoriopalama.udacity.popularmovies.persistence.PopularMoviesContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel that holds informations on the movies' list
 *
 * @author Gregorio Palamà
 */
public class MoviesListViewModel extends ViewModel {
    public static final int SORT_ORDER_POPULAR = 0;
    public static final int SORT_ORDER_TOP_RATED = 1;
    public static final int SORT_ORDER_FAVORITE = 2;

    private MutableLiveData<List<Movie>> movies;
    private MovieDbApi movieDbService;
    private MutableLiveData<Integer> sortOrder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MoviesListViewModel(MovieDbApi movieDbService) {
        this.movieDbService = movieDbService;
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

    public void retrievePopularMovies(ContentResolver contentResolver) {
        retrieveMovies(movieDbService.getPopularMovies(Constants.MOVIEDB_API_KEY), contentResolver);
    }

    public void retrieveTopRatedMovies(ContentResolver contentResolver) {
        retrieveMovies(movieDbService.getTopRatedMovies(Constants.MOVIEDB_API_KEY), contentResolver);
    }

    private void retrieveMovies(Observable<QueryResult> observable, ContentResolver contentResolver) {
        compositeDisposable.add(observable
                .map(result -> result.getResults())
                .map(movies -> {
                    for (Movie movie : movies) {
                        Cursor cursor = contentResolver
                                .query(PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                                        null,
                                        "_id=?",
                                        new String[]{Integer.toString(movie.getId())},
                                        null);
                        if (cursor.getCount() > 0) {
                            movie.setFavorite(true);
                        } else {
                            movie.setFavorite(false);
                        }
                    }

                    return movies;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<List<Movie>>() {
                    @Override
                    public void onNext(List<Movie> list) {
                        if (list.size() == 0 )
                            return;

                        movies.setValue(list);
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

    public void retrieveFavorites(ContentResolver contentResolver) {
        compositeDisposable.add(Single.create((SingleEmitter<Cursor> emitter) -> {
                    Cursor cursor = contentResolver
                            .query(PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(cursor);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Cursor>() {
                    @Override
                    public void onSuccess(Cursor cursor) {
                        movies.setValue(readMovieFromCursor(cursor));
                    }

                    private ArrayList<Movie> readMovieFromCursor(Cursor cursor) {
                        ArrayList<Movie> retMovies = new ArrayList<>();
                        for (int i = 0; i < cursor.getCount(); i++) {
                            int idIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry._ID);
                            int titleIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_TITLE);
                            int overviewIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_OVERVIEW);
                            int posterPathIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_POSTER_PATH);
                            int backdropPathIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_BACKDROP_PATH);
                            int voteAverageIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_VOTE_AVERAGE);
                            int voteCountIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_VOTE_COUNT);
                            int releaseDateIdx = cursor.getColumnIndex(PopularMoviesContract
                                    .FavoriteMovieEntry.COLUMN_NAME_RELEASE_DATE);

                            cursor.moveToPosition(i);

                            Movie movie = new Movie();
                            movie.setId(cursor.getInt(idIdx));
                            movie.setTitle(cursor.getString(titleIdx));
                            movie.setOverview(cursor.getString(overviewIdx));
                            movie.setPosterPath(cursor.getString(posterPathIdx));
                            movie.setBackdropPath(cursor.getString(backdropPathIdx));
                            movie.setVoteAverage(cursor.getFloat(voteAverageIdx));
                            movie.setVoteCount(cursor.getInt(voteCountIdx));
                            Date releaseDate = new Date();
                            releaseDate.setTime(cursor.getLong(releaseDateIdx));
                            movie.setReleaseDate(releaseDate);
                            movie.setFavorite(true);
                            retMovies.add(movie);
                        }
                        return retMovies;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }
}
