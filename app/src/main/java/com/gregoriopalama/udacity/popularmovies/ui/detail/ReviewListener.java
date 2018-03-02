package com.gregoriopalama.udacity.popularmovies.ui.detail;

import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolderListener;

/**
 * Listener interface for the Review. It extends GenericViewHolderListener
 * and makes possible to use a generic interface into the GenericViewHolder
 *
 * @see GenericViewHolderListener
 * @author Gregorio Palamà
 */
public interface ReviewListener extends GenericViewHolderListener {
    public void openReviewDetails();
    public void openReviewDetails(String reviewId);
}
