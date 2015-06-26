package io.mixrad.mixradioexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.mixrad.mixradiosdk.model.Artist;
import io.mixrad.mixradiosdk.model.Product;

/**
 * Created by mattaranha on 24/06/15.
 */
public class MixRadioOverviewFragment extends Fragment {

    Artist          mArtist;
    Product         mProduct;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_overview, container, false);


        ImageView imageView = (ImageView)rootView.findViewById(R.id.artist_icon);

        FragmentActivity fa = getActivity();
        if(fa instanceof MixRadioArtistActivity)
        {   MixRadioArtistActivity aFA = (MixRadioArtistActivity)fa;
            mArtist = aFA.mArtist;

            if(mArtist instanceof Artist)
            {   if(mArtist.thumb320Uri != null)
                {   Picasso.with(fa).load(mArtist.thumb320Uri).into(imageView);
                }
            }
        }
        else
        {   mArtist = null;
        }

        if(fa instanceof MixRadioProductActivity)
        {   MixRadioProductActivity pFA = (MixRadioProductActivity)fa;
            mProduct = pFA.mProduct;

            if(mProduct instanceof Product)
            {   if(mProduct.thumb320Uri != null)
            {   Picasso.with(fa).load(mProduct.thumb320Uri).into(imageView);
            }
            }
        }
        else
        {   mProduct = null;
        }






        return rootView;
    }
}
