package io.mixrad.mixradiosdk.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

        product.name = product_obj.get("name").getAsString();
        product.id = product_obj.get("id").getAsString();

        if (product_obj.has("thumbnails")) {
            JsonObject thumbnails = product_obj.get("thumbnails").getAsJsonObject();

            product.thumb50Uri = thumbnails.getAsJsonPrimitive("50x50").getAsString();
            product.thumb100Uri = thumbnails.getAsJsonPrimitive("100x100").getAsString();
            product.thumb200Uri = thumbnails.getAsJsonPrimitive("200x200").getAsString();
            product.thumb320Uri = thumbnails.getAsJsonPrimitive("320x320").getAsString();
        }

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


        return product;


    }
}
