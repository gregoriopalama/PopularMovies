package com.gregoriopalama.udacity.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Video Model
 *
 * @author Gregorio Palamà
 */
public class Video {
    public static final String VIDEO_TYPE_TRAILER = "Trailer";
    public static final String VIDEO_TYPE_TEASER = "Teaser";
    public static final String VIDEO_TYPE_CLIP = "Clip";
    public static final String VIDEO_TYPE_FEATURETTE = "Featurette";
    public static final List<String> VIDEO_TYPES = Arrays.asList(VIDEO_TYPE_TRAILER,
            VIDEO_TYPE_TEASER,
            VIDEO_TYPE_CLIP,
            VIDEO_TYPE_FEATURETTE);

    String id;

    @SerializedName("iso_639_1")
    String languageCode;

    @SerializedName("iso_3166_1")
    String countryCode;

    String key;
    String name;
    String site;
    int size;
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
