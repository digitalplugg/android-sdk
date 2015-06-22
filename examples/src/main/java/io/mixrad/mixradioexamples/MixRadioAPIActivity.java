package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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




public class MixRadioAPIActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_api);

        Intent intent = getIntent();
        MainActivity.MixRadioMode message = (MainActivity.MixRadioMode) intent.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        FragmentManager fm = getSupportFragmentManager();
        MixRadioGenericFragment genericFragment = new MixRadioGenericFragment();//(MixRadioGenericFragment) fm.findFragmentById(R.id.generic_fragment);

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.EXTRA_MESSAGE, message);
        genericFragment.setArguments(args);


        fm.beginTransaction().add(genericFragment, "FRAG").commit();
        fm.executePendingTransactions();


        Fragment temp = fm.findFragmentByTag("FRAG");


        //if(temp != null)
        //    genericFragment.populateView(message);
    }

    //*/
}
