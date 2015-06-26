package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import io.mixrad.mixradiosdk.MixRadioClient;


public class MainActivity extends FragmentActivity {

    public enum MixRadioMode {
        MixRadioMode_RequestCountry,
        MixRadioMode_Search,
        MixRadioMode_SearchArtist,

        MixRadioMode_Genres,
        MixRadioMode_TopAlbums,
        MixRadioMode_TopArtists,
        MixRadioMode_TopSongs,
        MixRadioMode_NewAlbums,
        MixRadioMode_NewSongs,
        MixRadioMode_SimilarArtists,
        MixRadioMode_SimilarProducts,
        MixRadioMode_GetMixGroups,
        MixRadioMode_GetMixes,
        MixRadioMode_Recommendations,

    }

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private static MixRadioClient               mixRadioClient;
    private static MediaPlayer                  mMediaPlayer;
    MixRadioSDKPagerAdapter                     mMixRadioSDKPagerAdapter;
    ViewPager                                   mViewPager;
    MixRadioAPIFragment                         fragmentAPI;


    public final static String MixRadioClientId = "0ffa393383247d8ed7835e72de69f6a8";
    public final static String EXTRA_MESSAGE = "io.mixrad.mixradioexplorer.MESSAGE";
    public final static int ITEMS_PER_PAGE = 30;

    public static MediaPlayer getMediaPlayer()
    {
        if(mMediaPlayer == null)
        {
            mMediaPlayer = new MediaPlayer();
        }

        return mMediaPlayer;
    }

    public static MixRadioClient getMixRadioClient()
    {
        if(mixRadioClient == null)
        {
            mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId);
        }

        return mixRadioClient;
    }

    public static MixRadioClient getNewMixRadioClient()
    {
        mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId);
        return mixRadioClient;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ActionBar actionBar = getActionBar();
        ArrayList<Fragment> fragment_list = new ArrayList<Fragment>();

        fragmentAPI = new MixRadioAPIFragment();
        fragment_list.add(fragmentAPI);
        fragment_list.add(new MixRadioUserDataFragment());

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mMixRadioSDKPagerAdapter =
                new MixRadioSDKPagerAdapter(
                        getSupportFragmentManager(),fragment_list);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMixRadioSDKPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
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

        // Add 3 tabs, specifying the tab's text and TabListener

        actionBar.addTab(
                actionBar.newTab()
                        .setText("API")

                        .setTabListener(tabListener));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("User Data")

                        .setTabListener(tabListener));




        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.

    }

    public void requestQueryMode(MixRadioMode mode)
    {
        if( mode == MixRadioMode.MixRadioMode_Search ||
            mode == MixRadioMode.MixRadioMode_SearchArtist )
        {
            Intent intent = new Intent(this, MixRadioSearchActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mode);
            startActivity(intent);
        }
        else
        /*if(mode == MixRadioMode.MixRadioMode_Genres)
        {
            Intent intent = new Intent(this, MixRadioGenresActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mode);
            startActivity(intent);
        }
        else
        */
        {
            Intent intent = new Intent(this, MixRadioAPIActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mode);
            startActivity(intent);
        }
    }

    public void buttonPressed(View view) {

        MixRadioMode tempMode = MixRadioMode.MixRadioMode_Search;

        switch(view.getId()) {
            case R.id.button_reset_country:
                tempMode = MixRadioMode.MixRadioMode_RequestCountry;
                break;
            case R.id.button_search_artists:
                tempMode = MixRadioMode.MixRadioMode_SearchArtist;
                break;
            case R.id.button_search:
                tempMode = MixRadioMode.MixRadioMode_Search;
                break;
            case R.id.button_top_artists:
                tempMode = MixRadioMode.MixRadioMode_TopArtists;
                break;
            case R.id.button_genres:
                tempMode = MixRadioMode.MixRadioMode_Genres;
                break;
            case R.id.button_top_albums:
                tempMode = MixRadioMode.MixRadioMode_TopAlbums;
                break;
            case R.id.button_new_albums:
                tempMode = MixRadioMode.MixRadioMode_NewAlbums;
                break;
            case R.id.button_get_mix_groups:
                tempMode = MixRadioMode.MixRadioMode_GetMixGroups;
                break;
        }

        if(view.getId() == R.id.button_reset_country)
        {
            mixRadioClient = new MixRadioClient(MainActivity.MixRadioClientId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragmentAPI)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(fragmentAPI)
                    .commit();



        }
        else
        {
            requestQueryMode(tempMode);
        }


    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class MixRadioSDKPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    public MixRadioSDKPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}



