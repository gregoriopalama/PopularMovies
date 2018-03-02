package com.gregoriopalama.udacity.popularmovies.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.popularmovies.model.Video;
import com.gregoriopalama.udacity.popularmovies.databinding.MovieVideoBinding;
import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolder;
import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolderListener;

import java.util.List;

/**
 * Adapter for the videos in the list. It uses a GenericViewHolder
 *
 * @see GenericViewHolder
 * @author Gregorio Palamà
 */
public class VideoAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    List<Video> items;
    GenericViewHolderListener listener;

    public VideoAdapter(List<Video> items, GenericViewHolderListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Video> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        MovieVideoBinding binding =
                MovieVideoBinding.inflate(layoutInflater, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
