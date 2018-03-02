package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gregoriopalama.udacity.popularmovies.model.Review;

import java.util.List;

import javax.inject.Inject;

/**
 * ViewModel that holds information about a Movie's reviews
 *
 * @author Gregorio Palamà
 */
public class ReviewsListViewModel extends ViewModel {
    private MutableLiveData<List<Review>> reviews;

    private Application application;

    @Inject
    public ReviewsListViewModel(Application application) {
        this.application = application;
        this.reviews = new MutableLiveData<>();
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews.setValue(reviews);
    }
}
