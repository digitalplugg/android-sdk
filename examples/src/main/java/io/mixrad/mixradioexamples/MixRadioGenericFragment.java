package io.mixrad.mixradioexamples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.mixrad.mixradiosdk.MixRadioClient;
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
import retrofit.client.Response;

/**
 * Created by mattaranha on 22/06/15.
 */
public class MixRadioGenericFragment extends Fragment {

    MixRadioClient              mixRadioClient;
    ArrayList<Object>           mixRadioData;
    MixRadioAdapter             adapter;
    ProgressDialog              progress;
    Activity                    mActivity;
    ListView                    listview;

    MainActivity.MixRadioMode   message;
    Genre                       mGenre;
    Artist                      mArtist;
    Product                     mProduct;
    MixGroup                    mMixGroup;

    public final static String  MIX_GROUP = "io.mixrad.mixradioexplorer.MIXGROUP";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        mixRadioData = new ArrayList<Object>();

        View rootView = inflater.inflate(
                R.layout.fragment_generic, container, false);

        listview = (ListView) rootView.findViewById(R.id.listview);

        adapter = new MixRadioAdapter(getActivity().getApplicationContext(), R.layout.list_item, mixRadioData);
        listview.setAdapter(adapter);

        FragmentActivity fa = getActivity();
        if(fa instanceof MixRadioGenresActivity)
        {   MixRadioGenresActivity gFA = (MixRadioGenresActivity)fa;
            mGenre = gFA.mGenre;
        }
        else
        {    mGenre = null;
        }

        if(fa instanceof MixRadioArtistActivity)
        {   MixRadioArtistActivity aFA = (MixRadioArtistActivity)fa;
            mArtist = aFA.mArtist;
        }
        else
        {   mArtist = null;
        }

        if(fa instanceof MixRadioProductActivity)
        {   MixRadioProductActivity pFA = (MixRadioProductActivity)fa;
            mProduct = pFA.mProduct;
        }
        else
        {   mProduct = null;
        }


        if(getArguments() != null)
        {
            message = (MainActivity.MixRadioMode) getArguments().getSerializable(MainActivity.EXTRA_MESSAGE);

            mMixGroup = (MixGroup)getArguments().get(MIX_GROUP);

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
                else if(message == MainActivity.MixRadioMode.MixRadioMode_TopArtists ||
                        message == MainActivity.MixRadioMode.MixRadioMode_SimilarArtists)
                {
                    Artist a = (Artist) mixRadioData.get(position);

                    Intent intent = new Intent(getActivity(), MixRadioArtistActivity.class);
                    intent.putExtra(MainActivity.EXTRA_MESSAGE, new Gson().toJson(a));
                    startActivity(intent);
                }
                else if(message == MainActivity.MixRadioMode.MixRadioMode_NewAlbums ||
                        message == MainActivity.MixRadioMode.MixRadioMode_TopAlbums)
                {
                    Product p = (Product) mixRadioData.get(position);

                    Intent intent = new Intent(getActivity(), MixRadioProductActivity.class);
                    intent.putExtra(MainActivity.EXTRA_MESSAGE, new Gson().toJson(p));
                    startActivity(intent);
                }
                else if(message == MainActivity.MixRadioMode.MixRadioMode_NewSongs ||
                        message == MainActivity.MixRadioMode.MixRadioMode_TopSongs)
                {
                    Product p = (Product) mixRadioData.get(position);
                    String url = mixRadioClient.getTrackSampleUri(p.id);

                    try {
                        MediaPlayer mediaPlayer = MainActivity.getMediaPlayer();


                        if(mediaPlayer.isPlaying())
                            mediaPlayer.stop();

                        mediaPlayer = MainActivity.getMediaPlayer();

                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else if(message == MainActivity.MixRadioMode.MixRadioMode_GetMixGroups)
                {
                    MixGroup mg = (MixGroup) mixRadioData.get(position);

                    Intent intent = new Intent(getActivity(), MixRadioMixesActivity.class);
                    intent.putExtra(MIX_GROUP, new Gson().toJson(mg));
                    startActivity(intent);
                }
            }
        });



        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        mActivity = activity;
    }


    public void populateView(MainActivity.MixRadioMode message)
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.search_title));

        mixRadioClient = MainActivity.getMixRadioClient();

        if(mixRadioClient == null)
        {   //Toast.makeText(getString(R.string.network_failed),Toast.LENGTH_LONG).show();
            return;
        }

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
                        if (progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if (progress != null) progress.cancel();
                    }
                });
            }
            else if(mArtist != null)
            {
                mixRadioClient.getArtistProducts(null, mArtist, Category.TRACK, OrderBy.RELEASE_DATE, SortOrder.ASCEND, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
                    @Override
                    public void success(List<Product> products, Response response) {
                        mixRadioData.clear();
                        mixRadioData.addAll(products);
                        adapter.notifyDataSetChanged();
                        if (progress != null) progress.cancel();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Toast.makeText(getActivity().getApplicationContext(), retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                        if (progress != null) progress.cancel();
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
        else if(message == MainActivity.MixRadioMode.MixRadioMode_SimilarArtists && mArtist != null)
        {
            mixRadioClient.getSimilarArtists(null, mArtist, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Artist>>() {
                @Override
                public void success(List<Artist> artists, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(artists);
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
        else if(message == MainActivity.MixRadioMode.MixRadioMode_SimilarProducts && mProduct != null)
        {
            mixRadioClient.getSimilarProducts(null, mProduct, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<Product>>() {
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
        else if(message == MainActivity.MixRadioMode.MixRadioMode_GetMixGroups)
        {
            mixRadioClient.getMixGroups(0, MainActivity.ITEMS_PER_PAGE, new Callback<List<MixGroup>>() {
                @Override
                public void success(List<MixGroup> mixGroups, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(mixGroups);
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
        else if(message == MainActivity.MixRadioMode.MixRadioMode_GetMixes)
        {
            mixRadioClient.getMixes(null, mMixGroup, 0, MainActivity.ITEMS_PER_PAGE, new Callback<List<MixClass>>() {
                @Override
                public void success(List<MixClass> mixClasses, Response response) {
                    mixRadioData.clear();
                    mixRadioData.addAll(mixClasses);
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
