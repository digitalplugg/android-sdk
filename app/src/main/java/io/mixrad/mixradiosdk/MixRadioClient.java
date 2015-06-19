package io.mixrad.mixradiosdk;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.mixrad.mixradiosdk.Util.ArtistDeserializer;
import io.mixrad.mixradiosdk.Util.ArtistSerializer;
import io.mixrad.mixradiosdk.Util.MusicItemDeserializer;
import io.mixrad.mixradiosdk.Util.ProductDeserializer;
import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MixClass;
import io.mixrad.mixradiosdk.model.MixGroup;
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;
import io.mixrad.mixradiosdk.model.UserEvent;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by RichardW on 05/03/15.
 */
public class MixRadioClient {


    private static final String BASE_URL = "http://api.mixrad.io/1.x";
    private static final String SECURE_URL = "https://sapi.mixrad.io/1.x";
    private final MixRadioService apiService;
    private final MixRadioService secureApiService;

    public MixRadioClient(String apiKey, String countryCode) {




        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .registerTypeAdapter(Artist.class, new ArtistSerializer())
                .registerTypeAdapter(Artist.class, new ArtistDeserializer())
                .registerTypeAdapter(MusicItem.class, new MusicItemDeserializer())
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .create();

        GsonConverter converter = new GsonConverter(gson);
        MixRadioInterceptor intercept = new MixRadioInterceptor();
        intercept.setApiKey(apiKey);
        intercept.setCountryCode(countryCode);

        MixRadioUserInterceptor userIntercept = new MixRadioUserInterceptor();
        userIntercept.setUserId("");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(converter)
                .setRequestInterceptor(intercept)
                /*
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        switch (retrofitError.getResponse().getStatus())
                        {
                            case 400:
                                throw new RuntimeException("Bad Request: "+retrofitError.getBody());
                            default:
                                throw new RuntimeException("Something else bad: "+retrofitError.getBody());
                        }
                    }
                })
                */
                .build();

        RestAdapter secureRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(SECURE_URL)
                .setConverter(converter)
                .setRequestInterceptor(userIntercept)
                .build();

        apiService = restAdapter.create(MixRadioService.class);
        secureApiService = secureRestAdapter.create(MixRadioService.class);
    }

    public MixRadioService getApiService() {
        return apiService;
    }

    public void search(String searchTerm, Category category, String genreId, String orderBy, String sortOrder, int startIndex, int itemsPerPage, Callback<List<MusicItem>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("q", searchTerm);
        if(category != null)
            options.put("category", category.toString());
        options.put("genreId", genreId);
        options.put("orderBy", orderBy);
        options.put("sortOrder", sortOrder);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);
        apiService.search(options, callback);
    }

    public void searchArtists(String searchTerm, int startIndex, int itemsPerPage, Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("q", searchTerm);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);
        apiService.searchArtists(options, callback);
    }

    public void getTopArtists(Callback<List<Artist>> callback) {
        getTopArtists(0, 20, callback);
    }
    public void getTopArtists(int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);
        options.put("category",Category.ARTIST.toString());

        apiService.getTopArtists(options, callback);
    }

    public void getTopProducts(Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopProducts(category, options, callback);
    }

    public void getNewReleases(Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getNewReleases(category, options, callback);
    }

    public void getNewReleasesForGenre(String id, Genre genre,Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String genre_id = "";
        if(id != null && !id.equals("")) {
            genre_id = id;
        } else if(genre != null) {
            genre_id = genre.id;
        }
        //options.put("category", category.toString());
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);


        apiService.getNewReleasesForGenre(genre_id, category,options, callback);
    }

    public void getGenres(Callback<List<Genre>> callback) {


        apiService.getGenres(callback);
    }

    public void getTopArtistsForGenre(String id, Genre genre, int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        if(id != null && !id.equals("")) {
            options.put("id", id);
        } else if(genre != null) {
            options.put("genre",""+genre.id);
        }
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        options.put("category", Category.ARTIST.toString());

        apiService.getTopArtistsForGenre(options, callback);
    }

    public void getTopProductsForGenre(String id, Genre genre,Category category, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String genre_id = "";
        if(id != null && !id.equals("")) {
            genre_id = id;
        } else if(genre != null) {
            genre_id = genre.id;
        }
        options.put("category", category.toString());
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getTopProductsForGenre(genre_id, category,options, callback);
    }

    public void getArtistProducts(String id, Artist artist,Category category, String orderBy, String sortOrder, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
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

        apiService.getArtistProducts(artist_id, options, callback);
    }


    public void getSimilarArtists(String id, Artist artist, int startIndex,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String artist_id = "";
        if(id != null && !id.equals("")) {
            artist_id = id;
        } else if(artist != null) {
            artist_id = artist.id;
        }
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSimilarArtists(artist_id, options, callback);
    }

    public void getMixGroups(int startIndex,int itemsPerPage,Callback<List<MixGroup>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getMixGroups(options, callback);
    }


    public void getMixes(String id, MixGroup group,int startIndex,int itemsPerPage,Callback<List<MixClass>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        String group_id = "";
        if(id != null && !id.equals("")) {
            group_id = id;
        } else if(group != null) {
            group_id = group.id;
        }

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getMixes(group_id, options, callback);
    }

    public void getArtistSearchSuggestions(String searchTerm, int itemsPerPage,Callback<List<String>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("q", ""+searchTerm);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getArtistSearchSuggestions(options, callback);
    }

    public void getSearchSuggestions(String searchTerm, int itemsPerPage,Callback<List<String>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("q", ""+searchTerm);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSearchSuggestions(options, callback);
    }

    public void getArtistsAroundLocation(double latitude, double longitude, int maxDistance, int startIndex, int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("location", ""+latitude+","+longitude);
        options.put("maxDistance", ""+maxDistance);
        options.put("startIndex", ""+startIndex);
        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getArtistsAroundLocation(options, callback);
    }


    public void getSimilarProducts(String id, Product product, int startIndex,int itemsPerPage,Callback<List<Product>> callback) {
        Map<String, String> options = new HashMap<String, String>();
        if((id == null || id.equals("")) && product!=null) {
            id = product.id;
        }

        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        apiService.getSimilarProducts(id, options, callback);
    }

    public Uri getTrackSampleUri(String id) {

        return apiService.getTrackSampleUri(id);
    }

    public void getUserPlayHistory(String action,  int startIndex,int itemsPerPage,Callback<List<UserEvent>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("action", ""+action);
        options.put("startIndex", ""+startIndex);

        options.put("itemsPerPage", ""+itemsPerPage);

        secureApiService.getUserPlayHistory(options, callback);
    }

    public void getUserTopArtists(String startDate, String endDate,int itemsPerPage,Callback<List<Artist>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startDate", ""+startDate);
        options.put("endDate", ""+endDate);
        options.put("itemsPerPage", ""+itemsPerPage);

        secureApiService.getUserTopArtists(options, callback);
    }

    public void getUserRecentMixes(String userid, String startDate, String endDate,int itemsPerPage,Callback<List<MixClass>> callback) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("startDate", ""+startDate);
        options.put("endDate", ""+endDate);
        options.put("itemsPerPage", ""+itemsPerPage);

        secureApiService.getUserRecentMixes(options, callback);
    }

    public String getAuthenticationUri(String scope, String state) {

        return SECURE_URL+"/authorize?response_type=code&state="+state+"&scope="+scope;


    }

    public void getAuthenticationToken(String clientSecret, String authCode) {
        Map<String, String> options = new HashMap<String, String>();

        options.put("clientSecret", ""+clientSecret);

        options.put("authCode", authCode);

        secureApiService.getAuthenticationToken(options);
    }

}
