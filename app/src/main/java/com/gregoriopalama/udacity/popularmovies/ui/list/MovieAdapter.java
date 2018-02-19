package com.gregoriopalama.udacity.popularmovies.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.popularmovies.api.dto.Movie;
import com.gregoriopalama.udacity.popularmovies.databinding.ItemMovieBinding;
import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolder;
import com.gregoriopalama.udacity.popularmovies.viewmodel.MoviesListViewModel;

import java.util.List;

/**
 * Adapter for the movies in the list. It uses a GenericViewHolder
 *
 * @see GenericViewHolder
 * @author Gregorio Palamà
 */

public class MovieAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private List<Movie> items;
    private MoviesListViewModel viewModel;

    public MovieAdapter(List<Movie> items, MoviesListViewModel viewModel) {
        this.items = items;
        this.viewModel = viewModel;
    }

    public void setItems(List<Movie> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ItemMovieBinding binding =
                ItemMovieBinding.inflate(layoutInflater, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bind(items.get(position), viewModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
