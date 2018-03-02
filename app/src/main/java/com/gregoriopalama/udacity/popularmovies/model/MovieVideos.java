package com.gregoriopalama.udacity.popularmovies.model;

import java.util.List;

/**
 * MovieVideos Model
 *
 * @author Gregorio Palamà
 */
public class MovieVideos {
    int id;
    List<Video> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
