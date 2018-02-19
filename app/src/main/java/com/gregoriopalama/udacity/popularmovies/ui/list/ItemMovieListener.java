package com.gregoriopalama.udacity.popularmovies.ui.list;

import com.gregoriopalama.udacity.popularmovies.api.dto.Movie;
import com.gregoriopalama.udacity.popularmovies.ui.GenericViewHolderListener;

/**
 * Listener interface for the Movie. It extends GenericViewHolderListener
 * and makes possible to use a generic interface into the GenericViewHolder
 *
 * @see GenericViewHolderListener
 * @author Gregorio Palamà
 */

public interface ItemMovieListener extends GenericViewHolderListener {
    void openDetail(Movie movie);
}
