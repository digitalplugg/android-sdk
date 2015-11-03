package io.mixrad.mixradiosdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RichardW on 04/03/15.
 */
public class Artist extends MusicItem {

    /** The artist’s country of origin (when available). */
    @SerializedName("country")
    public String country;
    /** The artist’s genres. */
    @SerializedName("genres")
    public List<Genre> genres;
    /** Gets the artist’s approximate origin geo-coordinates (when available).*/
    @SerializedName("origin")
    public Location location;

    /** Launches MixRadio to start a mix for the artist. */
    public void playMix() {
        return;
    }

    /** Launches MixRadio to show details for the artist. */
    public void show() {
        return;
    }

}
