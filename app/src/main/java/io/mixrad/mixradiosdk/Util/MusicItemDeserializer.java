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
    public MusicItem deserialize(JsonElement musicItemStr, Type typeOfSrc, JsonDeserializationContext context) {


        JsonObject jsonObject = musicItemStr.getAsJsonObject();

        String type = jsonObject.get("type").getAsString();

        MusicItem musicItem;

        if(type.equals("musicartist"))
        {
            Artist artist = context.deserialize(musicItemStr, Artist.class);
            musicItem = (MusicItem)artist;
        }
        else if(type.equals("musiccollection") || type.equals("musictrack"))
        {
            Product product = context.deserialize(musicItemStr, Product.class);
            musicItem = (MusicItem)product;
        }
        else
        {
            musicItem = new MusicItem();
        }


        musicItem = MusicItemDeserializer.readStandardItems(musicItem, jsonObject);

        return musicItem;

    }

    public static MusicItem readStandardItems(MusicItem item, JsonObject jsonobj)
    {
        item.name = jsonobj.get("name").getAsString();
        item.id = jsonobj.get("id").getAsString();

        if (jsonobj.has("thumbnails")) {
            JsonObject thumbnails = jsonobj.get("thumbnails").getAsJsonObject();

            item.thumb50Uri = thumbnails.getAsJsonPrimitive("50x50").getAsString();
            item.thumb100Uri = thumbnails.getAsJsonPrimitive("100x100").getAsString();
            item.thumb200Uri = thumbnails.getAsJsonPrimitive("200x200").getAsString();
            item.thumb320Uri = thumbnails.getAsJsonPrimitive("320x320").getAsString();
        }

        return item;
    }
}
