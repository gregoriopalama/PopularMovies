package com.gregoriopalama.udacity.popularmovies.ui.bindingadapters;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.gregoriopalama.udacity.popularmovies.R;

/**
 * Adapter used to create a roundIcon with a single character inside itself
 *
 * @author Gregorio Palamà
 */
public class NameIconBindingAdapter {
    @BindingAdapter({"round_icon_name"})
    public static void loadRoundIconName(ImageView imageView, String name) {
        if (name.equals(""))
            return;

        if (name.length() > 1) {
            name = name.substring(0, 1);
        }

        TextDrawable roundIcon = TextDrawable.builder()
                .buildRound(name.toUpperCase(), R.color.primary_dark_color);
        imageView.setImageDrawable(roundIcon);
    }
}
