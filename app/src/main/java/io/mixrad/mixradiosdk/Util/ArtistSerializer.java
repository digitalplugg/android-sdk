package io.mixrad.mixradiosdk.Util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.mixrad.mixradiosdk.model.Artist;

/**
 * Created by RichardW on 27/05/15.
 */
public class ArtistSerializer implements JsonSerializer<Artist> {

    public JsonElement serialize(Artist artist, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(artist.toString());
    }
}

