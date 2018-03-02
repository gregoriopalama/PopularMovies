package com.gregoriopalama.udacity.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.model.Movie;
import com.gregoriopalama.udacity.popularmovies.databinding.ActivityMainBinding;
import com.gregoriopalama.udacity.popularmovies.ui.ConnectivityUtils;
import com.gregoriopalama.udacity.popularmovies.ui.detail.DetailActivity;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel.SORT_ORDER_FAVORITE;
import static com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel.SORT_ORDER_POPULAR;
import static com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel.SORT_ORDER_TOP_RATED;

/**
 * The Main Activity. It shows the movies' lists
 *
 * @author Gregorio Palamà
 */
public class MainActivity extends AppCompatActivity implements ItemMovieListener {
    private final static int REQUEST_FAVORITE = 100;
    public final static String EXTRA_MOVIE_ID = "com.gregoriopalama.udacity.popularmovies.movie_id_extra";

    @Inject
    ViewModelFactory popularMoviesViewModelFactory;

    MoviesListViewModel moviesListViewModel;

    ActivityMainBinding binding;
    MovieAdapter adapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        moviesListViewModel = ViewModelProviders.of(this, popularMoviesViewModelFactory)
                .get(MoviesListViewModel.class);

        setupBottomNavigation();
        setupSwipeToRefresh();
        setupUI();
        setupObservers();

        if (!ConnectivityUtils.isConnected(getApplicationContext())
                && getSortOrder() != SORT_ORDER_FAVORITE) {
            setupUiOffline();
            return;
        }

        setupOnline();
    }

    private void setupObservers() {
        moviesListViewModel.getMovies().observe(this, (movies) -> {
            adapter.setItems(movies);
            binding.progress.setVisibility(View.GONE);

            if (movies.size() == 0
                && getSortOrder() == SORT_ORDER_FAVORITE) {
                binding.noFavorites.setVisibility(View.VISIBLE);
            } else {
                binding.moviesSwipeRefresh.setVisibility(View.VISIBLE);
            }
        });
        moviesListViewModel.getSortOrder()
                .observe(this, sortOrder -> refreshMovies(sortOrder));
    }

    private void setupSwipeToRefresh() {
        binding.moviesSwipeRefresh.setOnRefreshListener(() -> {
            refreshMovies(getSortOrder());
            binding.moviesSwipeRefresh.setRefreshing(false);
        });
        binding.moviesSwipeRefresh.setColorSchemeResources(R.color.secondary_color,
                R.color.primary_dark_color,
                R.color.primary_light_color);
    }

    private void setupOnline() {
        binding.moviesList.setVisibility(View.VISIBLE);
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        binding.offlineImage.setVisibility(View.GONE);
        binding.offlineMessage.setVisibility(View.GONE);
        binding.offlineTrynow.setVisibility(View.GONE);

        updateSortOrder();
    }

    private void refreshMovies(int sortOrder) {
        binding.moviesSwipeRefresh.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        binding.noFavorites.setVisibility(View.GONE);
        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                moviesListViewModel.retrievePopularMovies(getContentResolver());
                return;
            case SORT_ORDER_TOP_RATED:
                moviesListViewModel.retrieveTopRatedMovies(getContentResolver());
                return;
            case SORT_ORDER_FAVORITE:
                moviesListViewModel.retrieveFavorites(getContentResolver());
                return;
        }
    }

    private void setupUiOffline() {
        binding.moviesList.setVisibility(View.GONE);
        binding.bottomNavigation.setVisibility(View.GONE);
        binding.offlineImage.setVisibility(View.VISIBLE);
        binding.offlineMessage.setVisibility(View.VISIBLE);
        binding.offlineTrynow.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
    }

    public void setupUI() {
        binding.moviesList.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.movies_list_columns)));
        binding.moviesList.setHasFixedSize(true);
        adapter = new MovieAdapter(new ArrayList<>(), this);
        binding.moviesList.setAdapter(adapter);
        binding.offlineTrynow.setOnClickListener((view) -> {
            if (ConnectivityUtils.isConnected(getApplicationContext())) {
                setupOnline();
            }
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
            switch ( item.getItemId()) {
                case R.id.sort_popular:
                    setSortOrder(SORT_ORDER_POPULAR);
                    break;
                case R.id.sort_top_rated:
                    setSortOrder(SORT_ORDER_TOP_RATED);
                    break;
                case R.id.sort_favorite:
                    setSortOrder(SORT_ORDER_FAVORITE);
                    break;
            }
            item.setChecked(true);

            if (!ConnectivityUtils.isConnected(getApplicationContext())
                    && item.getItemId() != R.id.sort_favorite) {
                setupUiOffline();
            }

            return true;
        });
        switch (getSortOrder()) {
            case SORT_ORDER_POPULAR:
                binding.bottomNavigation.setSelectedItemId(R.id.sort_popular);
                break;
            case SORT_ORDER_TOP_RATED:
                binding.bottomNavigation.setSelectedItemId(R.id.sort_top_rated);
                break;
            case SORT_ORDER_FAVORITE:
                binding.bottomNavigation.setSelectedItemId(R.id.sort_favorite);
                break;
        }
    }

    public void setSortOrder(int sortOrder) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.sort_order_pref), sortOrder);
        editor.apply();

        updateSortOrder();
    }

    public void updateSortOrder() {
        moviesListViewModel.setSortOrder(getSortOrder());
    }

    public int getSortOrder() {
        int defaultSortOrder = getResources().getInteger(R.integer.sort_order_default);
        return sharedPref.getInt(getString(R.string.sort_order_pref), defaultSortOrder);
    }

    @Override
    public void openDetail(Movie movie, ImageView imageView) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.DETAIL_MOVIE_EXTRA_NAME, movie);

        String transitionName = getString(R.string.movie_transition_string);

        View viewStart = imageView;

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, viewStart, transitionName);
        ActivityCompat
                .startActivityForResult(this, intent, REQUEST_FAVORITE, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_FAVORITE) {
            if (!data.hasExtra(EXTRA_MOVIE_ID))
                return;

            int movieId = data.getIntExtra(EXTRA_MOVIE_ID, 0);
            if (movieId == 0)
                return;

            if (getSortOrder() == SORT_ORDER_FAVORITE) {
                moviesListViewModel.retrieveFavorites(getContentResolver());
            } else {
                moviesListViewModel.refreshFavoriteStatusByMovieId(movieId, getContentResolver());
                adapter.notifyDataSetChanged();
            }
        }
    }

}
