package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import io.mixrad.mixradiosdk.MixRadioClient;
import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.Product;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by RichardW on 01/06/15.
 */




public class MixRadioAPIActivity extends Activity {

    MixRadioClient          mixRadioClient;
    ArrayList<Object>       mixRadioData;
    MixRadioAdapter         adapter;
    ProgressDialog          progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId, "gb");
        mixRadioData = new ArrayList<Object>();

        setContentView(R.layout.activity_genericlist);

        // Create the list view
        final ListView listview = (ListView) findViewById(R.id.listview);

        adapter = new MixRadioAdapter(this, new ArrayList<Object>());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });


        Intent intent = getIntent();
        MainActivity.MixRadioMode message = (MainActivity.MixRadioMode) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        Log.d("Explorer", "changing to " + message);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.search_title));

        if(message == MainActivity.MixRadioMode.MixRadioMode_TopArtists)
        {
            progress.show();

            mixRadioClient.getTopArtists(new Callback<List<Artist>>() {
                @Override
                public void success(List<Artist> artists, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(artists);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Log.e("TEST", "ErrorM: " + retrofitError.getResponse().getStatus() + " " + retrofitError.getResponse().getBody());
                    Toast.makeText(MixRadioAPIActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }
        else if(message == MainActivity.MixRadioMode.MixRadioMode_Genres)
        {
            progress.show();

            mixRadioClient.getGenres(new Callback<List<Genre>>() {
                @Override
                public void success(List<Genre> genres, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(genres);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioAPIActivity.this,retrofitError.getResponse().getReason(),Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }

        else if(message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums)
        {
            progress.show();

            mixRadioClient.getTopProducts(Category.ALBUM, 0, 30, new Callback<List<Product>>() {
                @Override
                public void success(List<Product> products, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(products);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioAPIActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }
        else if(message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums)
        {
            progress.show();

            mixRadioClient.getNewReleases(Category.ALBUM, 0, 30, new Callback<List<Product>>() {
                @Override
                public void success(List<Product> products, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(products);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(MixRadioAPIActivity.this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
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


    //*/
}
