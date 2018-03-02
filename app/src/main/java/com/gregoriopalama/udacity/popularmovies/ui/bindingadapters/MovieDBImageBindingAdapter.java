package com.gregoriopalama.udacity.popularmovies.ui.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.gregoriopalama.udacity.popularmovies.Constants;
import com.gregoriopalama.udacity.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Adapter that binds Picasso loader into a ImageView. It also implements a responsive
 * loader that loads different image's resolution based on the width of the screen
 *
 * @author Gregorio Palamà
 */
public class MovieDBImageBindingAdapter {

    @BindingAdapter({"moviedb_image_url"})
    public static void loadImageSmall(ImageView imageView, String url) {
        if (url.equals(""))
            return;

        loadMovieDBImage(imageView,
                url,
                imageView.getContext().getResources().getString(R.string.image_size_dir));
    }

    @BindingAdapter({"moviedb_image_url_big"})
    public static void loadImageBig(ImageView imageView, String url) {
        if (url.equals(""))
            return;

        loadMovieDBImage(imageView,
                url,
                imageView.getContext().getResources().getString(R.string.image_size_dir_toolbar));
    }

    /**
     * It loads the image at the given url address. The url is build upon the MovieDb base
     * url and a dimension, that is base on the screen size
     *
     * @param imageView the imageView where to load the image into
     * @param url the url to the image
     * @param image_dir the directory of the size of the image we want to load
     */
    public static void loadMovieDBImage(ImageView imageView, String url, String image_dir) {
        Uri builtUri = Uri.parse(Constants.MOVIEDB_IMAGE_BASE_URL).buildUpon()
                .appendPath(image_dir)
                .appendEncodedPath(url)
                .build();

        Picasso.with(imageView.getContext()).load(builtUri.toString())
                .placeholder(R.drawable.ic_movie)
                .into(imageView);
    }

    @BindingAdapter(value={"youtube_thumb"})
    public static void loadYoutubeThumb(ImageView imageView, String videoId) {
        if (videoId.equals(""))
            return;

        Uri builtUri = Uri.parse(Constants.YOUTUBE_THUMB_BASE_URL).buildUpon()
                .appendPath(videoId)
                .appendPath(imageView.getContext().getResources().getString(R.string.youtube_thumb_size))
                .build();

        Picasso.with(imageView.getContext()).load(builtUri.toString())
                .placeholder(R.drawable.ic_movie)
                .into(imageView);
    }
}
