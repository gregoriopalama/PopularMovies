package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.api.MovieDbApi;
import com.gregoriopalama.udacity.popularmovies.model.Movie;
import com.gregoriopalama.udacity.popularmovies.model.MovieReviews;
import com.gregoriopalama.udacity.popularmovies.model.MovieVideos;
import com.gregoriopalama.udacity.popularmovies.model.Review;
import com.gregoriopalama.udacity.popularmovies.model.Video;
import com.gregoriopalama.udacity.popularmovies.persistence.PopularMoviesContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel that holds information about a single movie
 *
 * @author Gregorio Palamà
 */
public class MovieViewModel extends ViewModel {
    private MovieDbApi movieDbService;
    MutableLiveData<Movie> movie;
    MutableLiveData<List<Video>> videos;
    MutableLiveData<List<Review>> reviews;
    MutableLiveData<String> actualVideoKey;
    MutableLiveData<Integer> videosNumber;
    MutableLiveData<Integer> reviewsNumber;
    MutableLiveData<Boolean> favorite;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MovieViewModel(MovieDbApi movieDbService) {
        this.movieDbService = movieDbService;
        this.movie = new MutableLiveData<>();
        this.videos = new MutableLiveData<>();
        this.reviews = new MutableLiveData<>();
        this.actualVideoKey = new MutableLiveData<>();
        this.actualVideoKey.setValue("");
        this.videosNumber = new MutableLiveData<>();
        this.videosNumber.setValue(0);
        this.reviewsNumber = new MutableLiveData<>();
        this.reviewsNumber.setValue(0);
        this.favorite = new MutableLiveData<>();
        this.favorite.setValue(false);
    }

    public Movie getMovie() {
        return movie.getValue();
    }

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);
        retrieveMovieVideos();
        retrieveMovieReviews();
        this.favorite.setValue(movie.isFavorite());
    }

    public MutableLiveData<List<Video>> getVideos() {
        return videos;
    }

    public MutableLiveData<List<Review>> getReviews() {
        return reviews;
    }

    public MutableLiveData<Boolean> getObservableFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite.setValue(favorite);
        this.movie.getValue().setFavorite(favorite);
    }

    private void retrieveMovieVideos() {
        compositeDisposable
                .add(movieDbService
                        .getMovieVideos(this.movie.getValue().getId(), Constants.MOVIEDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieVideos>() {
                    @Override
                    public void onNext(MovieVideos movieVideos) {
                        if (movieVideos.getResults().size() == 0 )
                            return;

                        videos.setValue(new ArrayList<>());

                        for (Video video : movieVideos.getResults()) {
                            videos.getValue().add(video);
                        }
                        videosNumber.setValue(videos.getValue().size());
                        updateDefaultVideoKey();
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

    private void retrieveMovieReviews() {
        compositeDisposable
                .add(movieDbService
                        .getMovieReviews(this.movie.getValue().getId(), Constants.MOVIEDB_API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieReviews>() {
                            @Override
                            public void onNext(MovieReviews movieReviews) {
                                if (movieReviews.getResults().size() == 0 )
                                    return;

                                reviews.setValue(new ArrayList<>());

                                for (Review review : movieReviews.getResults()) {
                                    reviews.getValue().add(review);
                                }
                                reviewsNumber.setValue(reviews.getValue().size());
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

    private String getDefaultVideoKey() {
        if (videos.getValue() == null || videos.getValue().size() == 0)
            return null;

        for (String type : Video.VIDEO_TYPES) {
            for (Video video : videos.getValue()) {
                if (video.getType().equals(type)) {
                    return video.getKey();
                }
            }
        }

        return null;
    }

    private void updateDefaultVideoKey() {
        actualVideoKey.setValue(getDefaultVideoKey());
    }

    public String getActualVideoKey() {
        if (actualVideoKey.getValue() == null || actualVideoKey.getValue().equals("")) {
            actualVideoKey.setValue(getDefaultVideoKey());
        }

        return actualVideoKey.getValue();
    }

    public MutableLiveData<String> getObservableActualVideoKey() {
        return actualVideoKey;
    }

    public MutableLiveData<Integer> getObservableVideosNumber() {
        return videosNumber;
    }

    public MutableLiveData<Integer> getObservableReviewsNumber() {
        return reviewsNumber;
    }

    public void setActualVideoKey(String actualVideoKey) {
        this.actualVideoKey.setValue(actualVideoKey);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }

    public void toggleFavorite(ContentResolver contentResolver) {
        if (getMovie().isFavorite()) {
            removeFromFavorites(contentResolver);
        } else {
            addToFavorites(contentResolver);
        }
    }

    private void addToFavorites(ContentResolver contentResolver) {
        compositeDisposable
                .add(Single.create((SingleEmitter<Uri> emitter) -> {
                    Uri uri = contentResolver
                            .insert(PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                                    buildMovieContentValues());
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(uri);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(uri != null) {
                            setFavorite(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    @NonNull
    private ContentValues buildMovieContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry._ID,
                getMovie().getId());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_TITLE,
                getMovie().getTitle());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_OVERVIEW,
                getMovie().getOverview());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_POSTER_PATH,
                getMovie().getPosterPath());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_BACKDROP_PATH,
                getMovie().getBackdropPath());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_VOTE_AVERAGE,
                getMovie().getVoteAverage());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_VOTE_COUNT,
                getMovie().getVoteCount());
        contentValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_NAME_RELEASE_DATE,
                getMovie().getReleaseDate().getTime());
        return contentValues;
    }

    private void removeFromFavorites(ContentResolver contentResolver) {
        String stringId = Integer.toString(getMovie().getId());
        final Uri uri = PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(stringId)
                .build();

        compositeDisposable
                .add(Single.create((SingleEmitter<Integer> emitter) -> {
                    int deleted = contentResolver.delete(uri, null, null);
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(deleted);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer deleted) {
                        if (deleted > 0) {
                            setFavorite(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }
}
