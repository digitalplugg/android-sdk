package io.mixrad.mixradioexamples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    Genre                   mGenre;

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

        FragmentActivity fa = getActivity();
        if(fa instanceof MixRadioGenresActivity)
        {
            MixRadioGenresActivity gFA = (MixRadioGenresActivity)fa;
            mGenre = gFA.mGenre;
        }
        else
        {
            mGenre = null;
        }


        if(getArguments() != null)
        {
            Log.d("FRAG", "GENERIC ARGS");
            message = (MainActivity.MixRadioMode) getArguments().getSerializable(MainActivity.EXTRA_MESSAGE);

            if(mActivity != null)
            {
                populateView(message);
            }

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if(message == MainActivity.MixRadioMode.MixRadioMode_Genres)
                {
                    Genre genre = (Genre) mixRadioData.get(position);

                    Intent intent = new Intent(getActivity(), MixRadioGenresActivity.class);
                    intent.putExtra(MainActivity.EXTRA_MESSAGE, new Gson().toJson(genre));
                    startActivity(intent);
                }
                else if(message == MainActivity.MixRadioMode.MixRadioMode_TopArtists)
                {

                }
                else if(message == MainActivity.MixRadioMode.MixRadioMode_NewSongs ||
                        message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums ||
                        message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums ||
                        message == MainActivity.MixRadioMode.MixRadioMode_TopSongs)
                {

                }
                /*
                Toast.makeText(getActivity().getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                        */
            }
        });



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

        progress = new ProgressDialog(getActivity());//(mActivity.getApplicationContext());
        progress.setMessage(getString(R.string.search_title));

        if(message == MainActivity.MixRadioMode.MixRadioMode_Genres)
        {
            if(progress != null) progress.show();

            mixRadioClient.getGenres(new Callback<List<Genre>>() {
                @Override
                public void success(List<Genre> genres, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(genres);
                    adapter.notifyDataSetChanged();
                    if(progress != null) progress.cancel();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(getActivity().getApplicationContext(),retrofitError.getResponse().getReason(),Toast.LENGTH_LONG).show();
                    progress.cancel();
                }
            });
        }
        if(message == MainActivity.MixRadioMode.MixRadioMode_TopArtists)
        {
            if(progress != null) progress.show();

            if(mGenre != null)
            {
                mixRadioClient.getTopArtistsForGenre(null , mGenre, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Artist>>() {
                    @Override
                    public void success(List<Artist> artists, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(artists);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e("TEST", "ErrorM: " + retrofitError.getResponse().getStatus() + " " + retrofitError.getResponse().getBody());
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }
            else
            {   mixRadioClient.getTopArtists(new Callback<List<Artist>>() {
                    @Override
                    public void success(List<Artist> artists, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(artists);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e("TEST", "ErrorM: " + retrofitError.getResponse().getStatus() + " " + retrofitError.getResponse().getBody());
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }


        }


        else if(message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums || message == MainActivity.MixRadioMode.MixRadioMode_TopSongs)
        {
            if(progress != null) progress.show();

            Category category = (message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums) ? Category.ALBUM : Category.TRACK;

            if(mGenre != null)
            {
                mixRadioClient.getTopProductsForGenre(null, mGenre, category, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
                    @Override
                    public void success(List<Product> products, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(products);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }
            else
            {
                mixRadioClient.getTopProducts(category, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
                    @Override
                    public void success(List<Product> products, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(products);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }


        }
        else if(message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums || message == MainActivity.MixRadioMode.MixRadioMode_NewSongs)
        {
            if(progress != null) progress.show();

            Category category = (message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums) ? Category.ALBUM : Category.TRACK;

            if(mGenre != null)
            {
                mixRadioClient.getNewReleasesForGenre(null, mGenre, category, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
                    @Override
                    public void success(List<Product> products, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(products);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }
            else
            {
                mixRadioClient.getNewReleases(category, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
                    @Override
                    public void success(List<Product> products, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(products);
                        adapter.notifyDataSetChanged();
                        if(progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if(progress != null) progress.cancel();
                    }
                });
            }


        }
    }

}
