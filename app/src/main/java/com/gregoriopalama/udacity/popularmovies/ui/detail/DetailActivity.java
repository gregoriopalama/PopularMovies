package com.gregoriopalama.udacity.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gregoriopalama.udacity.popularmovies.BR;
import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.databinding.ActivityDetailBinding;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MovieViewModel;

import javax.inject.Inject;

/**
 * Detail Activity. It shows the movie's details
 *
 * @author Gregorio Palamà
 */
public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;

    @Inject
    ViewModelFactory popularMoviesViewModelFactory;

    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        if (!getIntent().hasExtra(Constants.DETAIL_MOVIE_EXTRA_NAME)) {
            finish();
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieViewModel = ViewModelProviders.of(this, popularMoviesViewModelFactory)
                .get(MovieViewModel.class);
        movieViewModel.setMovie(getIntent().getParcelableExtra(Constants.DETAIL_MOVIE_EXTRA_NAME));

        setTitle(movieViewModel.getMovie().getTitle());

        setupUI();
    }

    private void setupUI() {
        binding.setVariable(BR.movieViewModel, movieViewModel);

        if (movieViewModel.getMovie().getOriginalTitle()
                .equals(movieViewModel.getMovie().getTitle())) {
            binding.originalTitleLbl.setVisibility(View.GONE);
            binding.originalTitle.setVisibility(View.GONE);
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
}
