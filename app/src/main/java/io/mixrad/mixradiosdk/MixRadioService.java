package io.mixrad.mixradiosdk;

import android.net.Uri;

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
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by RichardW on 04/03/15.
 */
interface MixRadioService {


    @GET("/{countrycode}")
    public void searchAsync(
            @QueryMap Map<String, String> options,
            Callback<List<MusicItem>> callback
    );

    @GET("/{countrycode}")
    public void searchArtistsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/")
    public void getTopArtistsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/products/charts/{category}")
    public void getTopProductsAsync(
            @Path("category") Category category,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/")
    public void getNewReleasesAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/")
    public void getNewReleasesForGenreAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/genres")
    public void getGenresAsync(
            Callback<List<Genre>> callback
    );

    @GET("/")
    public void getTopArtistsForGenreAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/")
    public void getTopProductsForGenreAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/creators/{id}/products")
    public void getArtistProductsAsync(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/creators/{id}/similar")
    public void getSimilarArtistsAsync(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/mixes/groups")
    public void getMixGroupsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<MixGroup>> callback
    );

    @GET("/{countrycode}/mixes/groups/{id}")
    public void getMixesAsync(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<MixClass>> callback
    );

    @GET("/{countrycode}/suggestions/creators")
    public void getArtistSearchSuggestionsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<String>> callback
    );

    @GET("/{countrycode}/suggestions")
    public void getSearchSuggestionsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<String>> callback
    );

    @GET("/")
    public void getArtistsAroundLocationAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/")
    public void getSimilarProductsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/")
    public Uri getTrackSampleUri(
            @Query("id") String id
    );

    @GET("/")
    public Uri getAuthenticationUri(
            @QueryMap Map<String, String> options
    );
    @GET("/")
    public void getAuthenticationTokenAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );
    @GET("/")
    public void setAuthenticationToken(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );
    @GET("/")
    public void refreshAuthenticationTokenAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    /*@GET("/")
    public void authenticationUserAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/")
    public void deleteAuthenticationTokenAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );*/
    @GET("/")
    public void getUserPlayHistoryAsync(
            @QueryMap Map<String, String> options,
            Callback<List<UserEvent>> callback
    );

    @GET("/")
    public void getUserTopArtistsAsync(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/")
    public void getUserRecentMixesAsync(
            @QueryMap Map<String, String> options,
            Callback<List<MixClass>> callback
    );

}
