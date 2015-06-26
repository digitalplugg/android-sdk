package io.mixrad.mixradioexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import io.mixrad.mixradioexamples.MainActivity.MixRadioMode;
import io.mixrad.mixradiosdk.MixRadioClient;

// Instances of this class are fragments representing a single
// object in our collection.
public class MixRadioAPIFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_api, container, false);

        MixRadioClient mixRadioClient = MainActivity.getMixRadioClient();
        final Button countryButton = (Button) rootView.findViewById(R.id.button_reset_country);

        if(mixRadioClient == null)
        {
            countryButton.setText(getString(R.string.reset_country));
        }
        else
        {
            countryButton.setText(""+getString(R.string.reset_country)+" "+MainActivity.getMixRadioClient().getCountryCode());
        }

        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MixRadioClient mixRadioClient = MainActivity.getNewMixRadioClient();

                if(mixRadioClient == null)
                {
                    countryButton.setText(getString(R.string.reset_country));
                }
                else
                {
                    countryButton.setText(""+getString(R.string.reset_country)+" "+MainActivity.getMixRadioClient().getCountryCode());
                }
            }
        });


        return rootView;
    }
}
