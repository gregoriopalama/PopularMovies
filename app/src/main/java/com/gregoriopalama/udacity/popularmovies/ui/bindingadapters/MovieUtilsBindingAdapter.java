package com.gregoriopalama.udacity.popularmovies.ui.bindingadapters;

import android.databinding.BindingAdapter;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.gregoriopalama.udacity.popularmovies.R;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Adapter used to map a Movie's release date and a Movie's vote average
 *
 * @author Gregorio Palamà
 */
public class MovieUtilsBindingAdapter {
    @BindingAdapter({"release_date"})
    public static void setReleaseDate(TextView textView, Date date) {
        if (date == null)
            return;

        java.text.DateFormat dateFormat
                = DateFormat.getDateFormat(textView.getContext());
        textView.setText(dateFormat.format(date));
    }

    @BindingAdapter({"vote_average"})
    public static void setVoteAverage(TextView textView, float average) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(textView.getContext().getResources()
                .getInteger(R.integer.movie_vote_average_maximum_digits));
        textView.setText(decimalFormat.format(average)
                .concat(textView.getContext().getResources()
                        .getString(R.string.movie_vote_avegare_base)));
    }
}
