package io.mixrad.mixradioexamples;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.mixrad.mixradiosdk.MixRadioClient;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MixClass;
import io.mixrad.mixradiosdk.model.MixGroup;
import io.mixrad.mixradiosdk.model.Product;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mattaranha on 25/06/15.
 */
public class MixRadioMixesActivity extends FragmentActivity{

    MixRadioClient                  mixRadioClient;
    ProgressDialog                  progress;
    ArrayList<Object>               mixRadioData;
    MixRadioAdapter                 adapter;
    ListView                        mSearchResultsView;

    ViewPager                       mViewPager;
    MixGroup                        mMixGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_generic);


        mixRadioClient = MainActivity.getMixRadioClient();
        mixRadioData = new ArrayList<Object>();

        adapter = new MixRadioAdapter(this, R.layout.list_item, mixRadioData);

        mSearchResultsView = (ListView) findViewById(R.id.listview);
        mSearchResultsView.setAdapter(adapter);

        Intent intent = getIntent();
        String groupString = intent.getStringExtra(MixRadioGenericFragment.MIX_GROUP);
        mMixGroup = new Gson().fromJson(groupString, MixGroup.class);

        setTitle(mMixGroup.name);

        if(mMixGroup != null)
        {
            if(progress != null) progress.show();

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
                    //Toast.makeText(this, retrofitError.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    if(progress != null) progress.cancel();
                }
            });
        }
    }

}
