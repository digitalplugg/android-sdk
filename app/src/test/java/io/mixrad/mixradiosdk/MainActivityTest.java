package io.mixrad.mixradiosdk; /**
 * Created by mattaranha on 23/07/15.
 */
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.Exception;
import java.util.List;

import io.mixrad.mixradiosdk.MixRadioActivity;
import io.mixrad.mixradiosdk.MixRadioClient;
import io.mixrad.mixradiosdk.Util.ArtistDeserializer;
import io.mixrad.mixradiosdk.Util.ArtistSerializer;
import io.mixrad.mixradiosdk.Util.ProductDeserializer;
import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MixClass;
import io.mixrad.mixradiosdk.model.MixGroup;
import io.mixrad.mixradiosdk.model.OrderBy;
import io.mixrad.mixradiosdk.model.Product;
import io.mixrad.mixradiosdk.model.SortOrder;
import retrofit.Callback;
import retrofit.RetrofitError;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {

    MixRadioClient client;
    Gson           gson;

    String myApiKey = "0ffa393383247d8ed7835e72de69f6a8";
    final int itemsPerPage = 10;
    final int startIndex = 0;

    @Before
    public void setup() throws Exception {

        client = new MixRadioClient(myApiKey, "gb");

        gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .registerTypeAdapter(Artist.class, new ArtistSerializer())
                .registerTypeAdapter(Artist.class, new ArtistDeserializer())
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .create();

        //activity = Robolectric.setupActivity(MixRadioActivity.class);
        //assertNotNull("MixRadioActivity is not instantiated", activity);
        assertTrue(true);
    }

    @Test
    public void testInvalidClientId() throws Exception
    {
        MixRadioClient invalidClient = new MixRadioClient("xxxxxx", "gb");

        invalidClient.getGenres(new Callback<List<Genre>>() {
            @Override
            public void success(List<Genre> genres, retrofit.client.Response response) {
                assertFalse("Should not be able to query with invalid client id", true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertFalse("Should not be able to query with invalid client id", false);
            }
        });
    }

    @Test
    public void testSearch() throws Exception
    {
        new Thread() {

            @Override
            public void run() {
                super.run();

                client.getSearchSuggestions("test", 10, new Callback<List<String>>() {
                    @Override
                    public void success(List<String> strings, retrofit.client.Response response) {
                        assertTrue("Search found", (strings != null || strings.size()>0 ));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        assertTrue("Search failed", false);
                    }
                });
            }


        }.start();

    }

    @Test
    public void testSearchArtists() throws Exception
    {
        client.searchArtists("sdsadsd", startIndex, itemsPerPage, new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertTrue("Artists not found", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artist Search failed", false);
            }
        });
    }

    @Test
    public void testSuggest() throws Exception
    {
        client.getSearchSuggestions("son", itemsPerPage, new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, retrofit.client.Response response) {
                assertTrue("Suggestions not found", (strings != null || strings.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Suggestions failed", false);
            }
        });
    }

    @Test
    public void testArtistSuggestion() throws Exception
    {
        client.getArtistSearchSuggestions("son", itemsPerPage, new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, retrofit.client.Response response) {
                assertTrue("Artist Suggestions not found", (strings != null || strings.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artist Suggestions failed", false);
            }
        });
    }

    @Test
    public void testTopProducts() throws Exception
    {
        client.getTopProducts(Category.ALBUM, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertTrue("Top Products not found", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Top Products failed", false);
            }
        });
    }

    @Test
    public void testInvalidCategory() throws Exception
    {
        client.getTopProducts(Category.ARTIST, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertTrue("Top Products not found", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Top Products failed", false);
            }
        });
    }


    @Test
    public void testSimilarProducts() throws Exception
    {
        client.getSimilarProducts("36995407", null, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertTrue("Similar Products not found", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Similar Products failed", false);
            }
        });


        String productStr = "{ 'category': { 'id': 'Album', 'name': 'Album' }, 'trackcount': 31, 'name': 'The Velvet Underground & Nico 45th Anniversary (Deluxe Edition)', 'genres': [ { 'id': 'Rock', 'name': 'Rock' } ], 'variousartists': false, 'streetreleasedate': '2012-01-01T00:00:00Z', 'thumbnails': { '50x50': 'https://assets.mixrad.io/u/1.0/image/285032138/?w=50&q=40', '100x100': 'https://assets.mixrad.io/u/1.0/image/285032138/?w=100&q=70', '200x200': 'https://assets.mixrad.io/u/1.0/image/285032138/?w=200&q=90', '320x320': 'https://assets.mixrad.io/u/1.0/image/285032138/?w=320&q=90' }, 'creators': { 'performers': [ { 'name': 'Nico', 'id': '348785' }, { 'name': 'The Velvet Underground', 'id': '1289' } ] }, 'storeuri': 'http://music.nokia.com/gb/r/product/-/-/36995407/', 'id': '36995407', 'type': 'musiccollection'}";
        Product product = gson.fromJson(productStr, Product.class);

        client.getSimilarProducts(null, product, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertTrue("Similar Products not found", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Similar Products failed", false);
            }
        });

    }

    @Test
    public void testTopArtists() throws Exception
    {
        client.getTopArtists(new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertTrue("Top Artists not found", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Top Artists failed", false);
            }
        });
    }

    @Test
    public void testArtistsAround() throws Exception
    {
        client.getArtistsAroundLocation(51.454513, -2.587910, 50, startIndex, itemsPerPage, new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertTrue("Artists Around should have Artists", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artists Around failed", false);
            }
        });
    }

    @Test
    public void testArtistsAroundInvalid() throws Exception
    {
        client.getArtistsAroundLocation(666.6, 666.6, 100, startIndex, itemsPerPage, new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertFalse("Artists Around should not have Artists", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artists Around failed", false);
            }
        });
    }

    @Test
    public void testArtistProducts() throws Exception
    {
        client.getArtistProducts("1289", null, Category.ARTIST, OrderBy.NAME, SortOrder.ASCEND, startIndex, itemsPerPage, new Callback<List<Product>>(){
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertFalse("Artists Products should have Products", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artists Products failed", false);
            }
        });

        String artistStr = "{ 'category': { 'id': 'Artist', 'name': 'Artist' }, 'name': 'The Velvet Underground', 'genres': [ { 'id': 'Rock', 'name': 'Rock' } ], 'mbid': '94b0fb9d-a066-4823-b2ec-af1d324bcfcf', 'sortname': 'Velvet Underground, The', 'thumbnails': { '640x640': 'https://assets.mixrad.io/asset/artists/640x640/1289.jpg', '50x50': 'https://assets.mixrad.io/asset/artists/50x50/1289.jpg', '320x320': 'https://assets.mixrad.io/asset/artists/320x320/1289.jpg', '200x200': 'https://assets.mixrad.io/asset/artists/200x200/1289.jpg', '120x120': 'https://assets.mixrad.io/asset/artists/120x120/1289.jpg', '100x100': 'https://assets.mixrad.io/asset/artists/100x100/1289.jpg' }, 'origin': { 'location': { 'lat': 40.78498, 'lng': -73.83425 }, 'name': 'New York City, United States' }, 'country': { 'id': 'US', 'name': 'US' }, 'storeuri': 'http://music.nokia.com/gb/r/artist/-/1289/', 'id': '1289', 'type': 'musicartist'}";
        Artist artist = gson.fromJson(artistStr, Artist.class);

        client.getArtistProducts(null, artist, Category.ALBUM, OrderBy.NAME, SortOrder.ASCEND, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertFalse("Artists Products should have Products", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Artists Products failed", false);
            }
        });
    }

    @Test
    public void testSimilarArtists() throws Exception
    {
        client.getSimilarArtists("1289", null, startIndex, itemsPerPage, new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertFalse("Similar Artists should have Artists", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Similar Artists failed", false);
            }
        });

        String artistStr = "{ 'category': { 'id': 'Artist', 'name': 'Artist' }, 'name': 'The Velvet Underground', 'genres': [ { 'id': 'Rock', 'name': 'Rock' } ], 'mbid': '94b0fb9d-a066-4823-b2ec-af1d324bcfcf', 'sortname': 'Velvet Underground, The', 'thumbnails': { '640x640': 'https://assets.mixrad.io/asset/artists/640x640/1289.jpg', '50x50': 'https://assets.mixrad.io/asset/artists/50x50/1289.jpg', '320x320': 'https://assets.mixrad.io/asset/artists/320x320/1289.jpg', '200x200': 'https://assets.mixrad.io/asset/artists/200x200/1289.jpg', '120x120': 'https://assets.mixrad.io/asset/artists/120x120/1289.jpg', '100x100': 'https://assets.mixrad.io/asset/artists/100x100/1289.jpg' }, 'origin': { 'location': { 'lat': 40.78498, 'lng': -73.83425 }, 'name': 'New York City, United States' }, 'country': { 'id': 'US', 'name': 'US' }, 'storeuri': 'http://music.nokia.com/gb/r/artist/-/1289/', 'id': '1289', 'type': 'musicartist'}";
        Artist artist = gson.fromJson(artistStr, Artist.class);

        client.getSimilarArtists(null, artist, startIndex, itemsPerPage, new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, retrofit.client.Response response) {
                assertFalse("Similar Artists should have Artists", (artists != null || artists.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Similar Artists failed", false);
            }
        });
    }


    @Test
    public void testNewReleases() throws Exception
    {
        client.getNewReleases(Category.ALBUM, startIndex, itemsPerPage, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, retrofit.client.Response response) {
                assertFalse("New Releases should have Products", (products != null || products.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("New Releases failed", false);
            }
        });
    }

    @Test
    public void testMixGroups() throws Exception
    {
        client.getMixGroups(startIndex, itemsPerPage, new Callback<List<MixGroup>>() {
            @Override
            public void success(List<MixGroup> mixGroups, retrofit.client.Response response) {
                assertTrue("Mix Groups should be found", (mixGroups != null || mixGroups.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Mix Groups failed", false);
            }
        });
    }

    @Test
    public void testMixes() throws Exception
    {
        client.getMixGroups(startIndex, itemsPerPage, new Callback<List<MixGroup>>() {
            @Override
            public void success(List<MixGroup> mixGroups, retrofit.client.Response response) {
                assertTrue("Mix Groups should be found", (mixGroups != null || mixGroups.size() > 0));

                client.getMixes(null, mixGroups.get(0), startIndex, itemsPerPage, new Callback<List<MixClass>>() {
                    @Override
                    public void success(List<MixClass> mixClasses, retrofit.client.Response response) {
                        assertTrue("Mixes should be found", (mixClasses != null || mixClasses.size() > 0));
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        assertTrue("Mixes failed", false);
                    }
                });
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Mixes failed", false);
            }
        });
    }

    @Test
    public void testGenres() throws Exception
    {
        client.getGenres(new Callback<List<Genre>>() {
            @Override
            public void success(List<Genre> genres, retrofit.client.Response response) {
                assertTrue("Genres should be found", (genres != null || genres.size() > 0));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                assertTrue("Genres failed", false);
            }
        });

    }
}