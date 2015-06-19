package io.mixrad.mixradioexamples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.mixrad.mixradiosdk.MixRadioClient;
import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import io.mixrad.mixradioexamples.MainActivity.MixRadioMode;

/**
 * Created by mattaranha on 18/06/15.
 */
public class MixRadioSearchActivity extends Activity {

    public final static String QUERY = "io.mixrad.mixradioexplorer.QUERY";

    MixRadioClient mixRadioClient;
    ProgressDialog progress;
    ArrayList<Object> mixRadioData;
    MixRadioAdapter adapter;
    Menu mMenu;
    SearchView mSearchView;
    ListView mSearchResultsView;

    MainActivity.MixRadioMode searchMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genericlist);

        mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId, "gb");

        Intent intent = getIntent();
        searchMode = (MixRadioMode) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        mixRadioData = new ArrayList<Object>();
        adapter = new MixRadioAdapter(this, new ArrayList<Object>());

        mSearchView = (SearchView) findViewById(R.id.search);
        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setOnQueryTextListener(onQueryTextListener);

        mSearchResultsView = (ListView) findViewById(R.id.listview);
        mSearchResultsView.setAdapter(adapter);

        mSearchResultsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = mixRadioData.get(position);

                // check types of the objects
                if(o instanceof String)
                {
                    performSearch((String)o);
                }
                else if(o instanceof Artist)
                {
                    Log.d("SEARCH", "Got Artist");
                }
                else if(o instanceof Product)
                {
                    Log.d("SEARCH", "Got Product");
                }
                else if(o instanceof Genre)
                {
                    Log.d("SEARCH", "Got Genre");
                }
            }
        });


    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String arg0) {
            performSearch(arg0);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String arg0) {
            performSuggestion(arg0);
            return false;
        }
    };


    private void performSuggestion(String query)
    {

        if(searchMode == MixRadioMode.MixRadioMode_Search)
        {
            mixRadioClient.getSearchSuggestions(query, 10, new Callback<List<String>>() {
                @Override
                public void success(List<String> strings, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(strings);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioSearchActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(searchMode == MixRadioMode.MixRadioMode_SearchArtist)
        {
            mixRadioClient.getArtistSearchSuggestions(query, 10, new Callback<List<String>>() {
                @Override
                public void success(List<String> strings, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(strings);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioSearchActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void performSearch(String query)
    {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.search_title));

        progress.show();

        if(searchMode == MixRadioMode.MixRadioMode_Search)
        {
            mixRadioClient.search(query, null, null, null, null, 0, 10, new Callback<List<MusicItem>>() {
                @Override
                public void success(List<MusicItem> musicItems, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(musicItems);
                    updateListView();
                    progress.cancel();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioSearchActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }
        else if(searchMode == MixRadioMode.MixRadioMode_SearchArtist)
        {
            mixRadioClient.searchArtists(query, 0, 10, new Callback<List<Artist>>() {
                @Override
                public void success(List<Artist> artists, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(artists);
                    updateListView();
                    progress.cancel();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioSearchActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }
    }

    private void updateListView()
    {
        adapter.clear();
        adapter.addAll(mixRadioData);
        adapter.notifyDataSetChanged();

        progress.cancel();
    }



}
