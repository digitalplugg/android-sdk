package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.mixrad.mixradiosdk.model.Genre;

/**
 * Created by mattaranha on 22/06/15.
 */
public class MixRadioGenresActivity extends FragmentActivity {

    MixRadioSDKPagerAdapter     mMixRadioSDKPagerAdapter;
    ViewPager                   mViewPager;

    Genre                       mGenre;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String genreString = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mGenre = new Gson().fromJson(genreString, Genre.class);

        setTitle(mGenre.name);

        final ActionBar actionBar = getActionBar();
        final ArrayList<Fragment> fragment_list = new ArrayList<Fragment>();

        MixRadioGenericFragment topAlbums = new MixRadioGenericFragment();
        MixRadioGenericFragment topArtist = new MixRadioGenericFragment();
        MixRadioGenericFragment topSongs = new MixRadioGenericFragment();
        MixRadioGenericFragment newAlbums = new MixRadioGenericFragment();
        MixRadioGenericFragment newSongs = new MixRadioGenericFragment();

        Bundle args = new Bundle();
        MainActivity.MixRadioMode mode = MainActivity.MixRadioMode.MixRadioMode_TopAlbums;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        topAlbums.setArguments(args);

        args = new Bundle();
        mode = MainActivity.MixRadioMode.MixRadioMode_TopAlbums;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        topArtist.setArguments(args);

        args = new Bundle();
        mode = MainActivity.MixRadioMode.MixRadioMode_TopSongs;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        topSongs.setArguments(args);

        args = new Bundle();
        mode = MainActivity.MixRadioMode.MixRadioMode_NewAlbums;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        newAlbums.setArguments(args);

        args = new Bundle();
        mode = MainActivity.MixRadioMode.MixRadioMode_NewSongs;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        newSongs.setArguments(args);

        fragment_list.add(topAlbums);
        fragment_list.add(topArtist);
        fragment_list.add(topSongs);
        fragment_list.add(newAlbums);
        fragment_list.add(newSongs);

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /*
        mMixRadioSDKPagerAdapter =
                new MixRadioGenrePagerAdapter(
                        getSupportFragmentManager());//,fragment_list);
                        */
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
                        .setText(getString(R.string.top_albums))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.top_artists))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.top_songs))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.new_albums))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.new_songs))
                        .setTabListener(tabListener)
        );
    }
}
