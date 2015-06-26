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
    public void search(
            @QueryMap Map<String, String> options,
            Callback<List<MusicItem>> callback
    );

    @GET("/{countrycode}")
    public void searchArtists(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}")
    public void getTopArtists(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/products/charts/{category}")
    public void getTopProducts(
            @Path("category") Category category,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/products/new/{category}")
    public void getNewReleases(
            @Path("category") Category category,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/genres/{genre}/new/{category}")
    public void getNewReleasesForGenre(
            @Path("genre") String genre,
            @Path("category") Category category,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/genres?domain=music")
    public void getGenres(
            Callback<List<Genre>> callback
    );

    @GET("/{countrycode}")
    public void getTopArtistsForGenre(

            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/genres/{genre}/charts/{category}")
    public void getTopProductsForGenre(
            @Path("genre") String genre,
            @Path("category") Category category,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/creators/{id}/products")
    public void getArtistProducts(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

    @GET("/{countrycode}/creators/{id}/similar")
    public void getSimilarArtists(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/mixes/groups")
    public void getMixGroups(
            @QueryMap Map<String, String> options,
            Callback<List<MixGroup>> callback
    );

    @GET("/{countrycode}/mixes/groups/{id}")
    public void getMixes(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<MixClass>> callback
    );

    @GET("/{countrycode}/suggestions/creators")
    public void getArtistSearchSuggestions(
            @QueryMap Map<String, String> options,
            Callback<List<String>> callback
    );

    @GET("/{countrycode}/suggestions")
    public void getSearchSuggestions(
            @QueryMap Map<String, String> options,
            Callback<List<String>> callback
    );

    @GET("/{countrycode}")
    public void getArtistsAroundLocation(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/{countrycode}/products/{id}/similar")
    public void getSimilarProducts(
            @Path("id") String id,
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );

//    @GET("/{countrycode}/products/{id}/sample")
//    public String getTrackSampleUri(
//            @Path("id") String id
//    );

    //@GET("/authorize/")
    //public String getAuthenticationUri(
    //        @QueryMap Map<String, String> options
    //);
    @GET("/token/")
    public void getAuthenticationToken(
            @QueryMap Map<String, String> options
    );
    /*@GET("/")
    public void setAuthenticationToken(
            @QueryMap Map<String, String> options,
            Callback<List<Product>> callback
    );*/
    @GET("/")
    public String refreshAuthenticationToken(
            @QueryMap Map<String, String> options
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
    @GET("/users/{userid}/history/")
    public void getUserPlayHistory(
            @QueryMap Map<String, String> options,
            Callback<List<UserEvent>> callback
    );

    @GET("/users/{userid}/history/creators/")
    public void getUserTopArtists(
            @QueryMap Map<String, String> options,
            Callback<List<Artist>> callback
    );

    @GET("/users/{userid}/history/mixes/")
    public void getUserRecentMixes(
            @QueryMap Map<String, String> options,
            Callback<List<MixClass>> callback
    );

}
