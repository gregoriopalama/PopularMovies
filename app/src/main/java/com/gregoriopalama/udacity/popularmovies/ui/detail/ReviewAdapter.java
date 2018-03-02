package com.gregoriopalama.udacity.popularmovies.ui.detail;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.popularmovies.model.Review;
import com.gregoriopalama.udacity.popularmovies.databinding.MovieReviewBinding;
import com.gregoriopalama.udacity.popularmovies.databinding.MovieReviewFullBinding;
import com.gregoriopalama.udacity.popularmovies.databinding.MovieReviewMoreBinding;
import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolder;

import java.util.List;

/**
 * Adapter for the reviews in the list. It uses a GenericViewHolder
 *
 * @see GenericViewHolder
 * @author Gregorio Palamà
 */
public class ReviewAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private final static int MAXIMUM_REVIEWS_NUMBER = 3;

    private final static int ITEM_TYPE_REVIEW = 1;
    private final static int ITEM_TYPE_MORE = 2;

    List<Review> items;
    ReviewListener listener;
    boolean showAllReviews;

    public ReviewAdapter(List<Review> items, boolean showAllReviews, ReviewListener listener) {
        this.items = items;
        this.listener = listener;
        this.showAllReviews = showAllReviews;
    }

    public ReviewAdapter(List<Review> items, boolean showAllReviews) {
        this.items = items;
        this.showAllReviews = showAllReviews;
    }

    public void setItems(List<Review> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        ViewDataBinding binding;
        if (showAllReviews) {
            binding = MovieReviewFullBinding.inflate(layoutInflater, parent, false);
        } else if (viewType == ITEM_TYPE_REVIEW) {
            binding = MovieReviewBinding.inflate(layoutInflater, parent, false);
        } else {
            binding = MovieReviewMoreBinding.inflate(layoutInflater, parent, false);
        }
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        if (showAllReviews) {
            holder.bind(items.get(position));
        } else if (showAllReviews || getItemViewType(position) == ITEM_TYPE_REVIEW)
            holder.bind(items.get(position), listener);
        else
            holder.bind(Integer.toString(position), listener);
    }

    @Override
    public int getItemCount() {
        if (showAllReviews)
            return items.size();

        if (items.size() <= MAXIMUM_REVIEWS_NUMBER)
            return items.size();

        return MAXIMUM_REVIEWS_NUMBER + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (showAllReviews)
            return ITEM_TYPE_REVIEW;

        if (items.size() < MAXIMUM_REVIEWS_NUMBER)
            return ITEM_TYPE_REVIEW;

        if (position >= MAXIMUM_REVIEWS_NUMBER)
            return ITEM_TYPE_MORE;

        return ITEM_TYPE_REVIEW;
    }
}
