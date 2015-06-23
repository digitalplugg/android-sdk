package io.mixrad.mixradiosdk.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.Location;

/**
 * Created by RichardW on 27/05/15.
 */
public class ArtistDeserializer implements JsonDeserializer<Artist> {
    public Artist deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {
        Artist artist = new Artist();
        JsonObject artist_obj = artistStr.getAsJsonObject();

        artist = (Artist) MusicItemDeserializer.readStandardItems(artist, artist_obj);

        if (artist_obj.has("origin")) {
            JsonObject origin = artist_obj.get("origin").getAsJsonObject();
            JsonObject location = origin.get("location").getAsJsonObject();

            artist.location = new Location();
            artist.location.latitude = location.get("lat").getAsDouble();//getAsJsonPrimitive("latitude");
            artist.location.longitude = location.get("lng").getAsDouble();//
            //origin.getAsJsonPrimitive("name").getAsString();
        }

        /*if (artist_obj.has("country")) {
            JsonObject country = artist_obj.get("country").getAsJsonObject();

            artist.location = country.getAsJsonPrimitive("name").getAsString();
        }*/

        if (artist_obj.has("genres") && artist_obj.get("genres").isJsonArray()) {
            JsonArray genres = artist_obj.get("genres").getAsJsonArray();

            artist.genres = new ArrayList<Genre>();
            for (int i = 0; i < genres.size(); i++) {
                Genre genre = context.deserialize(genres.get(i), Genre.class);

                artist.genres.add(genre);
            }

        }


        return artist;


    }
}
