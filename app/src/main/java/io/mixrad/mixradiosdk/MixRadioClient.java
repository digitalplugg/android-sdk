package io.mixrad.mixradiosdk;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MixClass;
import io.mixrad.mixradiosdk.model.MixGroup;
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;
import io.mixrad.mixradiosdk.model.UserEvent;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.Query;

/**
 * Created by RichardW on 05/03/15.
 */
public class MixRadioClient {


    private static final String BASE_URL = "http://api.mixrad.io/1.x";
    private final MixRadioService apiService;

    public MixRadioClient(String apiKey, String countryCode) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .registerTypeAdapter(Artist.class, new ArtistSerializer())
                .registerTypeAdapter(Artist.class, new ArtistDeserializer())
                .registerTypeAdapter(MusicItem.class, new MusicItemDeserializer())
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .create();

        GsonConverter convertor = new GsonConverter(gson);
        MixRadioInterceptor intercept = new MixRadioInterceptor();
        intercept.setApiKey(apiKey);
        intercept.setCountryCode(countryCode);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(convertor)
                .setRequestInterceptor(intercept)
                .build();

        apiService = restAdapter.create(MixRadioService.class);
    }

    public MixRadioService getApiService() {
        return apiService;
    }

    public void searchAsync(String searchTerm, Category category, String genreId, String orderBy, String sortOrder, int startIndex, int itemsPerPage, Callback<List<MusicItem>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("q", searchTerm);
        options.put("category", category.toString());
        options.put("genreId", genreId);
        options.put("orderBy", orderBy);
        options.put("sortOrder", sortOrder);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);
        apiService.searchAsync(options,callback);
    }

    public void searchArtistsAsync(String searchTerm, int startIndex, int itemsPerPage, Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("q", searchTerm);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);
        apiService.searchArtistsAsync(options,callback);
    }

    public void getTopArtistsAsync(Callback<List<Artist>> callback) {
        getTopArtistsAsync(0, 20, callback);
    }
    public void getTopArtistsAsync(int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopArtistsAsync(options, callback);
    }

    public void getTopProductsAsync(Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopProductsAsync(category,options, callback);
    }

    public void getNewReleasesAsync(Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("category", category.toString());
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getNewReleasesAsync(options, callback);
    }

    public void getNewReleasesForGenreAsync(String id, Genre genre,Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        if(id != null && !id.equals("")) {
            options.put("id", id);
        } else if(genre != null) {
            options.put("genre",""+genre.id);
        }
        options.put("category", category.toString());
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getNewReleasesForGenreAsync(options, callback);
    }

    public void getGenresAsync(Callback<List<Genre>> callback) {


        apiService.getGenresAsync(callback);
    }

    public void getTopArtistsForGenreAsync(String id, Genre genre, int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        if(id != null && !id.equals("")) {
            options.put("id", id);
        } else if(genre != null) {
            options.put("genre",""+genre.id);
        }
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopArtistsForGenreAsync(options, callback);
    }

    public void getTopProductsForGenreAsync(String id, Genre genre,Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        if(id != null && !id.equals("")) {
            options.put("id", id);
        } else if(genre != null) {
            options.put("genre",""+genre.id);
        }
        options.put("category", category.toString());
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopProductsForGenreAsync(options, callback);
    }

    public void getArtistProductsAsync(String id, Artist artist,Category category, String orderBy, String sortOrder, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String artist_id = "";
        if(id != null && !id.equals("")) {
            artist_id = id;
        } else if(artist != null) {
            artist_id = artist.id;
        }
        options.put("category", category.toString());
        options.put("orderBy", orderBy);
        options.put("sortOrder", sortOrder);
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getArtistProductsAsync(artist_id, options, callback);
    }


    public void getSimilarArtistsAsync(String id, Artist artist, int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String artist_id = "";
        if(id != null && !id.equals("")) {
            artist_id = id;
        } else if(artist != null) {
            artist_id = artist.id;
        }
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSimilarArtistsAsync(artist_id,options, callback);
    }

    public void getMixGroupsAsync(int startIndex,int itemsPerPage,Callback<List<MixGroup>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getMixGroupsAsync(options, callback);
    }


    public void getMixesAsync(String id, MixGroup group,int startIndex,int itemsPerPage,Callback<List<MixClass>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String group_id = "";
        if(id != null && !id.equals("")) {
            group_id = id;
        } else if(group != null) {
            group_id = group.id;
        }

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getMixesAsync(group_id,options, callback);
    }

    public void getArtistSearchSuggestionsAsync(String searchTerm, int itemsPerPage,Callback<List<String>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("searchTerm", ""+searchTerm);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getArtistSearchSuggestionsAsync(options, callback);
    }

    public void getSearchSuggestionsAsync(String searchTerm, int itemsPerPage,Callback<List<String>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("q", ""+searchTerm);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSearchSuggestionsAsync(options, callback);
    }

    public void getArtistsAroundLocationAsync(double latitude, double longitude, int maxDistance, int startIndex, int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("latitude", ""+latitude);
        options.put("longitude", ""+longitude);
        options.put("maxDistance", ""+maxDistance);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getArtistsAroundLocationAsync(options, callback);
    }


    public void getSimilarProductsAsync(String id, Product product, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        if(id != null && !id.equals("")) {
            options.put("id", id);
        } else if(product != null) {
            options.put("product",""+product.id);
        }

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSimilarProductsAsync(options, callback);
    }

    public Uri getTrackSampleUri(String id) {

        return apiService.getTrackSampleUri(id);
    }

    public void getUserPlayHistoryAsync(String action,  int startIndex,int itemsPerPage,Callback<List<UserEvent>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("action", ""+action);
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getUserPlayHistoryAsync(options, callback);
    }

    public void getUserTopArtistsAsync(String startDate, String endDate,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startDate", ""+startDate);
        options.put("endDate", ""+endDate);
        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getUserTopArtistsAsync(options, callback);
    }

    public void getUserRecentMixesAsync(String startDate, String endDate,int itemsPerPage,Callback<List<MixClass>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startDate", ""+startDate);
        options.put("endDate", ""+endDate);
        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getUserRecentMixesAsync(options, callback);
    }


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



    public class ArtistSerializer implements JsonSerializer<Artist> {

        public JsonElement serialize(Artist artist, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(artist.toString());
        }
    }



    public class ArtistDeserializer implements JsonDeserializer<Artist> {
        public Artist deserialize(JsonElement artistStr, Type typeOfSrc, JsonDeserializationContext context) {
            Artist artist = new Artist();
            JsonObject artist_obj = artistStr.getAsJsonObject();

            artist.name = artist_obj.get("name").getAsString();
            artist.id = artist_obj.get("id").getAsString();

            if (artist_obj.has("thumbnails")) {
                JsonObject thumbnails = artist_obj.get("thumbnails").getAsJsonObject();

                artist.thumb50Uri = thumbnails.getAsJsonPrimitive("50x50").getAsString();
                artist.thumb100Uri = thumbnails.getAsJsonPrimitive("100x100").getAsString();
                artist.thumb200Uri = thumbnails.getAsJsonPrimitive("200x200").getAsString();
                artist.thumb320Uri = thumbnails.getAsJsonPrimitive("320x320").getAsString();
            }

            if (artist_obj.has("origin")) {
                JsonObject origin = artist_obj.get("origin").getAsJsonObject();

                artist.location = origin.getAsJsonPrimitive("name").getAsString();
            }

            if (artist_obj.has("country")) {
                JsonObject country = artist_obj.get("country").getAsJsonObject();

                artist.location = country.getAsJsonPrimitive("name").getAsString();
            }

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

}
