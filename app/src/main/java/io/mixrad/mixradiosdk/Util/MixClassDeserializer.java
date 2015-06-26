package io.mixrad.mixradiosdk.Util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import io.mixrad.mixradiosdk.model.MixClass;
import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by mattaranha on 26/06/15.
 */
public class MixClassDeserializer implements JsonDeserializer<MixClass> {

    public MixClass deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {
        MixClass mixClass = new MixClass();

        JsonObject mix_obj = artistStr.getAsJsonObject();

        mixClass = (MixClass) MusicItemDeserializer.readStandardItems(mixClass, mix_obj);

        if(mix_obj.has("parentaladvisory")) {
            mixClass.parentalAdvisory = mix_obj.get("parentaladvisory").getAsBoolean();
        }

        return mixClass;
    }

}
