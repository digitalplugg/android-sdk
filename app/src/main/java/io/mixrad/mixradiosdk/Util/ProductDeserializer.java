package io.mixrad.mixradiosdk.Util;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by RichardW on 27/05/15.
 */
public class ProductDeserializer implements JsonDeserializer<Product> {
    public Product deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {
        Product product = new Product();
        JsonObject product_obj = artistStr.getAsJsonObject();

        product = (Product) MusicItemDeserializer.readStandardItems(product, product_obj);

        if(product_obj.has("category")) {
            JsonObject category = product_obj.get("category").getAsJsonObject();


            product.category = Category.valueOf(category.getAsJsonPrimitive("id").getAsString().toUpperCase());

        }

        if(product_obj.has("takenfrom")) {
            JsonObject takenfrom = product_obj.get("takenfrom").getAsJsonObject();

            Product taken = new Product();
            taken.id =  takenfrom.getAsJsonPrimitive("id").getAsString();
            taken.name =  takenfrom.getAsJsonPrimitive("name").getAsString();

            product.takenFrom = taken;

        }

        if(product_obj.has("trackcount")) {
            product.trackCount = product_obj.get("trackcount").getAsInt();
        }
        if (product_obj.has("genres") && product_obj.get("genres").isJsonArray()) {
            JsonArray genres = product_obj.get("genres").getAsJsonArray();

            product.genres = new ArrayList<Genre>();
            for (int i = 0; i < genres.size(); i++) {
                Genre genre = context.deserialize(genres.get(i), Genre.class);

                product.genres.add(genre);
            }

        }

        if(product_obj.has("creators") && product_obj.get("creators").isJsonObject())
        {
            JsonElement creators = product_obj.get("creators");
            JsonObject creatorsObj = creators.getAsJsonObject();

            JsonElement creatorsElem = (creatorsObj.has("performers")) ? creatorsObj.get("performers") : creatorsObj.get("composers");
            JsonArray creatorsArray = creatorsElem.getAsJsonArray();

            if(creatorsArray != null)
            {
                product.performers = new ArrayList<Artist>();

                for(int i=0; i<creatorsArray.size(); i++)
                {
                    Artist a = context.deserialize(creatorsArray.get(i), Artist.class);

                    product.performers.add(a);
                }
            }
        }


        return product;


    }
}
