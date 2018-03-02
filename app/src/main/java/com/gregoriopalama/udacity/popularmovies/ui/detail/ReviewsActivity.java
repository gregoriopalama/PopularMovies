package com.gregoriopalama.udacity.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.model.Review;
import com.gregoriopalama.udacity.popularmovies.databinding.ActivityReviewsBinding;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ReviewsListViewModel;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * The Reviews Activity. It shows a movie's reviews
 *
 * @author Gregorio Palamà
 */
public class ReviewsActivity extends AppCompatActivity {
    public static final String REVIEWS_LIST_EXTRA = "com.gregoriopalama.udacity.popularmovies.reviews_list";
    public static final String MOVIE_TITLE_EXTRA = "com.gregoriopalama.udacity.popularmovies.movie_title";
    public static final String REVIEW_ID_EXTRA = "com.gregoriopalama.udacity.popularmovies.review_id";

    ActivityReviewsBinding binding;

    @Inject
    ViewModelFactory popularMoviesViewModelFactory;
    ReviewsListViewModel reviewsListViewModel;

    ReviewAdapter reviewAdapter;
    LinearLayoutManager reviewsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reviews);

        if (!getIntent().hasExtra(REVIEWS_LIST_EXTRA)
                || !getIntent().hasExtra(MOVIE_TITLE_EXTRA)) {
            finish();
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupUI();

        reviewsListViewModel = ViewModelProviders.of(this, popularMoviesViewModelFactory)
                .get(ReviewsListViewModel.class);
        reviewsListViewModel.getReviews()
                .observe(this, (reviews) -> reviewAdapter.setItems(reviews));

        reviewsListViewModel.setReviews(getIntent().getParcelableArrayListExtra(REVIEWS_LIST_EXTRA));

        setTitle(TextUtils.concat(getString(R.string.reviews_lists_title),
                getIntent().getStringExtra(MOVIE_TITLE_EXTRA)));

        if (getIntent().hasExtra(REVIEW_ID_EXTRA)) {
            String reviewId = getIntent().getStringExtra(REVIEW_ID_EXTRA);
            for (Review review : reviewsListViewModel.getReviews().getValue()) {
                if (review.getId().equals(reviewId)) {
                    reviewsLayoutManager.scrollToPositionWithOffset(reviewsListViewModel
                                    .getReviews().getValue().indexOf(review),
                            getResources().getInteger(R.integer.review_detail_top_offset));
                }
            }
        }
    }

    private void setupUI() {
        reviewAdapter = new ReviewAdapter(new ArrayList<>(), true);
        reviewsLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        binding.reviewsList.setLayoutManager(reviewsLayoutManager);
        binding.reviewsList.addItemDecoration(new DividerItemDecoration(binding.reviewsList.getContext(),
                LinearLayoutManager.VERTICAL));
        binding.reviewsList.setAdapter(reviewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
