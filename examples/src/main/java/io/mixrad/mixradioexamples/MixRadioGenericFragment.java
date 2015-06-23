package io.mixrad.mixradioexamples;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.mixrad.mixradiosdk.MixRadioClient;
import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.Product;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mattaranha on 22/06/15.
 */
public class MixRadioGenericFragment extends Fragment {

    MixRadioClient          mixRadioClient;
    ArrayList<Object>       mixRadioData;
    MixRadioAdapter         adapter;
    ProgressDialog          progress;
    Activity                mActivity;
    ListView                listview;

    MainActivity.MixRadioMode   message;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("FRAG", "GENERIC ONCREATEVIEW");


        mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId, "gb");
        mixRadioData = new ArrayList<Object>();

        View rootView = inflater.inflate(
                R.layout.fragment_generic, container, false);

        listview = (ListView) rootView.findViewById(R.id.listview);

        adapter = new MixRadioAdapter(getActivity().getApplicationContext(), R.layout.list_item, mixRadioData);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        if(getArguments() != null)
        {
            Log.d("FRAG", "GENERIC ARGS");
            message = (MainActivity.MixRadioMode) getArguments().getSerializable(MainActivity.EXTRA_MESSAGE);

            if(mActivity != null)
            {
                populateView(message);
            }

        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {   Log.d("FRAG", "ATTACH");

        super.onAttach(activity);
        mActivity = activity;

        if(message != null) {
            //populateView(message);
        }
    }


    public void populateView(MainActivity.MixRadioMode message)
    {
        Log.d("FRAG", "GENERIC POPULATE");

        //progress = new ProgressDialog(mActivity.getApplicationContext());
        //progress.setMessage(getString(R.string.search_title));

        if(message == MainActivity.MixRadioMode.MixRadioMode_TopArtists)
        {
            //progress.show();

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
                    Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    //progress.cancel();
                }
            });
        }
        else if(message == MainActivity.MixRadioMode.MixRadioMode_Genres)
        {
            //progress.show();

            mixRadioClient.getGenres(new Callback<List<Genre>>() {
                @Override
                public void success(List<Genre> genres, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(genres);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(getActivity().getApplicationContext(),retrofitError.getResponse().getReason(),Toast.LENGTH_LONG).show();
                    //progress.cancel();
                }
            });
        }

        else if(message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums)
        {
            //progress.show();

            mixRadioClient.getTopProducts(Category.ALBUM, 0, 30, new Callback<List<Product>>() {
                @Override
                public void success(List<Product> products, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(products);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    //progress.cancel();
                }
            });
        }
        else if(message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums)
        {
            //progress.show();

            mixRadioClient.getNewReleases(Category.ALBUM, 0, 30, new Callback<List<Product>>() {
                @Override
                public void success(List<Product> products, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(products);
                    updateListView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    //progress.cancel();
                }
            });
        }
    }

    private void updateListView()
    {
        //adapter.clear();

        //adapter.addAll(mixRadioData);
        adapter.notifyDataSetChanged();

        listview.setAdapter(adapter);
        //progress.cancel();
    }
}
