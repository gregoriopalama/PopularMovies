package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.format.DateFormat;

import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.api.dto.Movie;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

/**
 * ViewModel that holds information about a single movie
 *
 * @author Gregorio Palamà
 */
public class MovieViewModel extends ViewModel {
    private static final String YEAR_FORMAT = "yyyy";

    private Application application;
    MutableLiveData<Movie> movie;

    @Inject
    public MovieViewModel(Application application) {
        this.application = application;
        this.movie = new MutableLiveData<>();
    }

    public Movie getMovie() {
        return movie.getValue();
    }

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);
    }

    public String getReleaseYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_FORMAT);
        return simpleDateFormat.format(movie.getValue().getReleaseDate());
    }

    public String getFormattedReleaseDate() {
        java.text.DateFormat dateFormat
                = DateFormat.getDateFormat(application.getApplicationContext());
        return dateFormat.format(movie.getValue().getReleaseDate());
    }

    public String getFormattedVoteAverage() {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(application.getResources()
                .getInteger(R.integer.movie_vote_average_maximum_digits));
        return decimalFormat.format(movie.getValue().getVoteAverage())
                .concat(application.getResources().getString(R.string.movie_vote_avegare_base));
    }
}
