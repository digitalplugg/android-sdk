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
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by RichardW on 27/05/15.
 */
public class MusicItemDeserializer implements JsonDeserializer<MusicItem> {
    public MusicItem deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {


        JsonObject artist_obj = artistStr.getAsJsonObject();

        String type = artist_obj.get("type").getAsString();

        MusicItem musicItem;

        if(type.equals("musicartist"))
        {
            Artist artist = new ArtistDeserializer().deserialize(artistStr, typeOfSrc, context);
            musicItem = (MusicItem)artist;
        }
        else if(type.equals("musiccollection") || type.equals("musictrack"))
        {
            Product product = new ProductDeserializer().deserialize(artistStr, typeOfSrc, context);
            musicItem = (MusicItem)product;
        }
        else
        {
            musicItem = new MusicItem();
        }

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
