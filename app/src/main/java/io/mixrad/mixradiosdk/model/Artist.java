package io.mixrad.mixradiosdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RichardW on 04/03/15.
 */
public class Artist {

        /** The ID of the artist */
        @SerializedName("id")
        public String id;
        /** The display name of the artist. */
        @SerializedName("name")
        public String name;
        /** The artist’s country of origin (when available). */
        @SerializedName("country")
        public String country;
        /** The artist’s genres. */
        @SerializedName("genres")
        public List<Genre> genres;
        /** Gets the artist’s approximate origin geocoordinates (when available).*/
        @SerializedName("origin")
        public String location;
        /** Gets a URI to a 50px x 50px thumbnail (when available). */
        @SerializedName("thumbnails")
        public String thumb50Uri;

        /** Gets a URI to a 100px x 100px thumbnail (when available). */
        public String thumb100Uri;

        /** Gets a URI to a 200px x 200px thumbnail (when available). */
        public String thumb200Uri;

        /** Gets a URI to a 320px x 320px thumbnail (when available). */
        public String thumb320Uri;

        /** Launches MixRadio to start a mix for the artist. */
        public void playMix() {
            return;
        }
        /** Launches MixRadio to show details for the artist. */
        public void show() {
            return;
        }

}
