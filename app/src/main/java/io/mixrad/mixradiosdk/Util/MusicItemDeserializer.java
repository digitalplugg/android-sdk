package io.mixrad.mixradiosdk.Util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import io.mixrad.mixradiosdk.model.MusicItem;

/**
 * Created by RichardW on 27/05/15.
 */
public class MusicItemDeserializer implements JsonDeserializer<MusicItem> {
    public MusicItem deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {
        MusicItem musicItem = new MusicItem();
        JsonObject artist_obj = artistStr.getAsJsonObject();

        musicItem.name = artist_obj.get("name").getAsString();
        musicItem.id = artist_obj.get("id").getAsString();

        if (artist_obj.has("thumbnails")) {
            JsonObject thumbnails = artist_obj.get("thumbnails").getAsJsonObject();

            musicItem.thumb50Uri = thumbnails.getAsJsonPrimitive("50x50").getAsString();
            musicItem.thumb100Uri = thumbnails.getAsJsonPrimitive("100x100").getAsString();
            musicItem.thumb200Uri = thumbnails.getAsJsonPrimitive("200x200").getAsString();
            musicItem.thumb320Uri = thumbnails.getAsJsonPrimitive("320x320").getAsString();
        }



        return musicItem;


    }
}
