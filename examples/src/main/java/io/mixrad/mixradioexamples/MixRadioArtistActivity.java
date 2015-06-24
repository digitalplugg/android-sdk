package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Genre;

/**
 * Created by mattaranha on 24/06/15.
 */
public class MixRadioArtistActivity  extends FragmentActivity {
    MixRadioSDKPagerAdapter     mMixRadioSDKPagerAdapter;
    ViewPager                   mViewPager;
    Artist                      mArtist;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String artistString = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mArtist = new Gson().fromJson(artistString, Artist.class);

        setTitle(mArtist.name);

        final ActionBar actionBar = getActionBar();
        final ArrayList<Fragment> fragment_list = new ArrayList<Fragment>();

        MixRadioArtistFragment artistOverview = new MixRadioArtistFragment();
        MixRadioGenericFragment topSongs = new MixRadioGenericFragment();
        MixRadioGenericFragment similarArtists = new MixRadioGenericFragment();

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.EXTRA_MESSAGE, new Gson().toJson(mArtist));
        artistOverview.setArguments(args);

        args = new Bundle();
        MainActivity.MixRadioMode mode = MainActivity.MixRadioMode.MixRadioMode_TopSongs;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        topSongs.setArguments(args);

        args = new Bundle();
        mode = MainActivity.MixRadioMode.MixRadioMode_SimilarArtists;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        similarArtists.setArguments(args);

        fragment_list.add(artistOverview);
        fragment_list.add(topSongs);
        fragment_list.add(similarArtists);

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mMixRadioSDKPagerAdapter = new MixRadioSDKPagerAdapter(getSupportFragmentManager(), fragment_list);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMixRadioSDKPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);

                        //((MixRadioGenericFragment)fragment_list.get(position)).populateView(MainActivity.MixRadioMode.MixRadioMode_TopArtists);
                    }
                });

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
        };

        // Add tabs, specifying the tab's text and TabListener


        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.artist_overview))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.top_songs))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.artist_similar))
                        .setTabListener(tabListener)
        );
    }
}
