package io.mixrad.mixradiosdk;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONObject;

import java.io.IOException;

import io.mixrad.mixradiosdk.model.Category;

/**
 * Created by RichardW on 05/03/15.
 */
public class ItemTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        final TypeAdapter<Category> categoryAdapter = gson.getAdapter(Category.class);
        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    Log.e("JSON", jsonElement.toString());
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.has("items") && jsonObject.get("items").isJsonArray())
                    {
                        jsonElement = jsonObject.get("items");
                        //Log.e("JSON ITEMS", jsonElement.toString());
                    } else if(jsonObject.has("results")  && jsonObject.get("results").isJsonArray()) {
                        jsonElement = jsonObject.get("results");
                    }
                    else if(jsonObject.has("radiostations")  && jsonObject.get("radiostations").isJsonArray()) {
                        jsonElement = jsonObject.get("radiostations");
                    }
                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}
