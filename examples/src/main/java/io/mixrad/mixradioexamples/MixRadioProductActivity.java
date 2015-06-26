package io.mixrad.mixradioexamples;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by mattaranha on 25/06/15.
 */
public class MixRadioProductActivity extends FragmentActivity {
    MixRadioSDKPagerAdapter     mMixRadioSDKPagerAdapter;
    ViewPager                   mViewPager;
    Product                     mProduct;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String productString = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mProduct = new Gson().fromJson(productString, Product.class);

        setTitle(mProduct.name);

        final ActionBar actionBar = getActionBar();
        final ArrayList<Fragment> fragment_list = new ArrayList<Fragment>();

        MixRadioOverviewFragment overview = new MixRadioOverviewFragment();
        MixRadioGenericFragment similarProducts = new MixRadioGenericFragment();

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.EXTRA_MESSAGE, new Gson().toJson(mProduct));
        overview.setArguments(args);

        args = new Bundle();
        MainActivity.MixRadioMode mode = MainActivity.MixRadioMode.MixRadioMode_SimilarProducts;
        args.putSerializable(MainActivity.EXTRA_MESSAGE, mode);
        similarProducts.setArguments(args);

        fragment_list.add(overview);
        fragment_list.add(similarProducts);

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
                        .setText(getString(R.string.album_overview))
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(getString(R.string.album_similar))
                        .setTabListener(tabListener)
        );
    }
}
