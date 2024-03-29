package io.mixrad.mixradiosdk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Category;
import io.mixrad.mixradiosdk.model.Genre;
import io.mixrad.mixradiosdk.model.MusicItem;
import io.mixrad.mixradiosdk.model.Product;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

/**
 * Created by RichardW on 04/03/15.
 */
public class MixRadioActivity extends Activity {

    private static final String TAG = "MIXRADIOACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);

        String myApiKey = "0ffa393383247d8ed7835e72de69f6a8";//getString(R.string.MixRadioClientId,"");

        final MixRadioClient client = new MixRadioClient(myApiKey, "gb");

        new Thread() {

            @Override
            public void run() {
                super.run();

                String test = client.getAuthenticationUri("play_mix write_usertasteprofile read_usertasteprofile read_userplayhistory", "");

                Log.e("SUCCESS","have returned "+test);
            }


        }.start();
        /*client.getSearchSuggestions("test", 10, new Callback<List<String>>() {
            @Override
            public void success(List<String> artists, Response response) {
                Log.e("SUCCESS", "have got a list of artists");
                for (int i = 0; i < artists.size(); i++) {
                    Log.e("SUCCESS", "" + artists.get(i));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e("FAILURE", "haven't got list of artists");
            }
        });*/

    }


}
