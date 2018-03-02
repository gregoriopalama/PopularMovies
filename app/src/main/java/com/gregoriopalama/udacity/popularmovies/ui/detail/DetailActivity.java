package com.gregoriopalama.udacity.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.gregoriopalama.udacity.popularmovies.BR;
import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.model.Review;
import com.gregoriopalama.udacity.popularmovies.databinding.ActivityDetailBinding;
import com.gregoriopalama.udacity.popularmovies.ui.ConnectivityUtils;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MovieViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.popularmovies.ui.detail.ReviewsActivity.MOVIE_TITLE_EXTRA;
import static com.gregoriopalama.udacity.popularmovies.ui.detail.ReviewsActivity.REVIEWS_LIST_EXTRA;
import static com.gregoriopalama.udacity.popularmovies.ui.detail.ReviewsActivity.REVIEW_ID_EXTRA;
import static com.gregoriopalama.udacity.popularmovies.ui.list.MainActivity.EXTRA_MOVIE_ID;

/**
 * Detail Activity. It shows the movie's details
 *
 * @author Gregorio Palamà
 */
public class DetailActivity extends AppCompatActivity implements VideoListener, ReviewListener {
    ActivityDetailBinding binding;

    @Inject
    ViewModelFactory popularMoviesViewModelFactory;

    MovieViewModel movieViewModel;
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    YouTubePlayer youTubePlayer;

    private int result = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if (!getIntent().hasExtra(Constants.DETAIL_MOVIE_EXTRA_NAME)) {
            finish();
        }

        movieViewModel = ViewModelProviders.of(this, popularMoviesViewModelFactory)
                .get(MovieViewModel.class);
        setupObservers();
        movieViewModel.setMovie(getIntent().getParcelableExtra(Constants.DETAIL_MOVIE_EXTRA_NAME));
        setTitle(movieViewModel.getMovie().getTitle());
        setupUI();

        setupYoutubeVideoFragment();
    }

    private void setupYoutubeVideoFragment() {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        youTubePlayerFragment.initialize(Constants.YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (wasRestored)
                    return;

                youTubePlayer = player;
                if (movieViewModel.getActualVideoKey() != null) {
                    if (binding.youtubeFragment.getVisibility() == View.GONE) {
                        binding.moviePosterBig.setVisibility(View.GONE);
                        binding.youtubeFragment.setVisibility(View.VISIBLE);
                    }
                    youTubePlayer.cueVideo(movieViewModel.getActualVideoKey());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
    }

    private void setupObservers() {
        movieViewModel.getVideos().observe(this, (videos) -> {
            videoAdapter.setItems(videos);

        });
        movieViewModel.getObservableActualVideoKey().observe(this, (key) -> {
            if (youTubePlayer != null && key != null) {
                if (binding.youtubeFragment.getVisibility() == View.GONE) {
                    binding.moviePosterBig.setVisibility(View.GONE);
                    binding.youtubeFragment.setVisibility(View.VISIBLE);
                    youTubePlayer.cueVideo(key);
                } else {
                    youTubePlayer.loadVideo(key);
                }
            }
        });
        movieViewModel.getObservableVideosNumber().observe(this, (videosNumber) -> {
            if (videosNumber > 0) {
                binding.videosCard.setVisibility(View.VISIBLE);
                binding.videosLbl.setVisibility(View.VISIBLE);
            }
        });
        movieViewModel.getObservableReviewsNumber().observe(this, (reviewNumber) -> {
            if (reviewNumber > 0) {
                binding.reviewsLbl.setVisibility(View.VISIBLE);
                binding.reviews.setVisibility(View.VISIBLE);
            }
        });
        movieViewModel.getReviews().observe(this, (reviews) -> {
            reviewAdapter.setItems(reviews);
            if (reviews.size()>0) {
                binding.reviewsLbl.setVisibility(View.VISIBLE);
                binding.reviews.setVisibility(View.VISIBLE);
            }
        });
        movieViewModel.getObservableFavorite().observe(this, (favorite) -> {
            if (favorite) {
                binding.favorite.setImageResource(R.drawable.ic_favorite);
            } else {
                binding.favorite.setImageResource(R.drawable.ic_favorite_border);
            }
            result = RESULT_OK;
        });
    }

    private void setupUI() {
        binding.setVariable(BR.movieViewModel, movieViewModel);

        binding.videos.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false));
        videoAdapter = new VideoAdapter(new ArrayList<>(), this);
        binding.videos.setAdapter(videoAdapter);

        binding.reviews.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false));
        reviewAdapter = new ReviewAdapter(new ArrayList<>(), false, this);
        binding.reviews.setAdapter(reviewAdapter);
        binding.reviews.addItemDecoration(new DividerItemDecoration(binding.reviews.getContext(),
                LinearLayoutManager.VERTICAL));

        binding.favorite.setOnClickListener((v) -> {
            movieViewModel.toggleFavorite(getContentResolver());
        });

        if (!ConnectivityUtils.isConnected(getApplicationContext())) {
            binding.noConnectionMessage.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void openVideo(String key) {
        this.movieViewModel.setActualVideoKey(key);
    }

    @Override
    public void openReviewDetails() {
        Intent intent = buildReviewsListIntent();
        startActivity(intent);
    }

    @Override
    public void openReviewDetails(String reviewId) {
        Intent intent = buildReviewsListIntent();
        intent.putExtra(REVIEW_ID_EXTRA, reviewId);
        startActivity(intent);
    }

    private Intent buildReviewsListIntent() {
        Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
        intent.putExtra(MOVIE_TITLE_EXTRA, movieViewModel.getMovie().getTitle());
        intent.putParcelableArrayListExtra(REVIEWS_LIST_EXTRA, (ArrayList<Review>)
                movieViewModel.getReviews().getValue());
        return intent;
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(EXTRA_MOVIE_ID, movieViewModel.getMovie().getId());
        setResult(result, data);
        super.finish();
    }
}
