package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by mattaranha on 22/06/15.
 */
public class MixRadioGenresActivity extends FragmentActivity {

    //MixRadioGenrePagerAdapter mMixRadioSDKPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        final ArrayList<Fragment> fragment_list = new ArrayList<Fragment>();

        MixRadioGenericFragment topArtist = new MixRadioGenericFragment();
        MixRadioGenericFragment topAlbums = new MixRadioGenericFragment();
        MixRadioGenericFragment topSongs = new MixRadioGenericFragment();
        MixRadioGenericFragment newAlbums = new MixRadioGenericFragment();
        MixRadioGenericFragment newSongs = new MixRadioGenericFragment();

        /*
        fragment_list.add(topArtist);
        fragment_list.add(topAlbums);
        fragment_list.add(topSongs);
        fragment_list.add(newAlbums);
        fragment_list.add(newSongs);
*/
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        /*
        mMixRadioSDKPagerAdapter =
                new MixRadioGenrePagerAdapter(
                        getSupportFragmentManager());//,fragment_list);
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
        */
        /*
        topArtist.populateView(MainActivity.MixRadioMode.MixRadioMode_TopArtists);
        topAlbums.populateView(MainActivity.MixRadioMode.MixRadioMode_TopAlbums);
        topSongs.populateView(MainActivity.MixRadioMode.MixRadioMode_TopSongs);
        newAlbums.populateView(MainActivity.MixRadioMode.MixRadioMode_NewAlbums);
        newSongs.populateView(MainActivity.MixRadioMode.MixRadioMode_NewSongs);
        */

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
                        .setText(getString(R.string.top_artists))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.top_albums))
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

    /*
    class MixRadioGenrePagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments;
        public MixRadioGenrePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            MixRadioGenericFragment f = new MixRadioGenericFragment();
            Bundle args = new Bundle();
            args.putSerializable(MainActivity.EXTRA_MESSAGE, MainActivity.MixRadioMode.MixRadioMode_TopAlbums);
            f.setArguments(args);
            return f;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }
    */
}
