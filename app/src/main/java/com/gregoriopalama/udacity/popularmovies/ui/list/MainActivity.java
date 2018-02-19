package com.gregoriopalama.udacity.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gregoriopalama.udacity.popularmovies.R;
import com.gregoriopalama.udacity.popularmovies.databinding.ActivityMainBinding;
import com.gregoriopalama.udacity.popularmovies.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel.SORT_ORDER_POPULAR;
import static com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel.SORT_ORDER_TOP_RATED;

/**
 * The Main Activity. It shows the movies' lists
 *
 * @author Gregorio Palamà
 */
public class MainActivity extends AppCompatActivity {

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

        binding.offlineTrynow.setOnClickListener((view) -> {
            if (isConnected()) {
                setupOnline();
            }
        });
        setupBottomNavigation();
        setupSwipeToRefresh();
        setupUI();

        moviesListViewModel.getMovies().observe(this, movies -> adapter.setItems(movies));
        moviesListViewModel.getSortOrder()
                .observe(this, sortOrder -> refreshMovies(sortOrder));

        if (!isConnected()) {
            setupUiOffline();
            return;
        }

        setupOnline();
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
        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                moviesListViewModel.retrievePopularMovies();
                return;
            case SORT_ORDER_TOP_RATED:
                moviesListViewModel.retrieveTopRatedMovies();
                return;
        }
    }

    private void setupUiOffline() {
        binding.moviesList.setVisibility(View.GONE);
        binding.bottomNavigation.setVisibility(View.GONE);
        binding.offlineImage.setVisibility(View.VISIBLE);
        binding.offlineMessage.setVisibility(View.VISIBLE);
        binding.offlineTrynow.setVisibility(View.VISIBLE);
    }

    public void setupUI() {
        binding.moviesList.setLayoutManager(new GridLayoutManager(getApplicationContext(),
                getResources().getInteger(R.integer.movies_list_columns)));
        binding.moviesList.setHasFixedSize(true);
        adapter = new MovieAdapter(new ArrayList<>(), moviesListViewModel);
        binding.moviesList.setAdapter(adapter);
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
            }
            item.setChecked(true);

            if (!isConnected()) {
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

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            return false;
        }

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
