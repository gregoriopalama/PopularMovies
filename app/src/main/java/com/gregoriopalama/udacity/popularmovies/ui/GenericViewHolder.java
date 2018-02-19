package com.gregoriopalama.udacity.popularmovies.ui;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.gregoriopalama.udacity.popularmovies.BR;

/**
 * Generic ViewHolder that uses all the power of the data binding library.
 * In order to use it, the view must contain a variable called "obj".
 * It also can bind a listener, and it must be called "listeners". The listener must
 * implements the interface GenericViewHolderListener
 *
 * @see GenericViewHolderListener
 * @author Gregorio Palamà
 */
public class GenericViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;
    public GenericViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.obj, obj);
        binding.executePendingBindings();
    }

    public void bind(Object obj, GenericViewHolderListener listeners) {
        binding.setVariable(BR.obj, obj);
        binding.setVariable(BR.listeners, listeners);
        binding.executePendingBindings();
    }

}


