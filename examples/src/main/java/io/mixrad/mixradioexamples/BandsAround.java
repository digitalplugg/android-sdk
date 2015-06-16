package io.mixrad.mixradioexamples;



    import com.google.android.gms.common.ConnectionResult;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.google.android.gms.location.LocationServices;
    import com.google.android.gms.maps.*;
    import com.google.android.gms.maps.model.*;
    import android.app.Activity;
    import android.location.Location;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Toast;

    import java.util.List;

    import io.mixrad.mixradiosdk.MixRadioClient;
    import io.mixrad.mixradiosdk.model.Artist;
    import retrofit.Callback;
    import retrofit.RetrofitError;
    import retrofit.client.Response;

public class BandsAround extends Activity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        private GoogleMap map;
    MapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);

            mapFragment= (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



        }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
        @Override
        public void onMapReady(final GoogleMap new_map) {
            this.map = new_map;
            final LatLng my_location = new LatLng(51.4500, -2.5833);

            this.map.setMyLocationEnabled(true);
            this.map.setOnCameraChangeListener(this);
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(my_location, 13));
            //this.map.setOnMarkerDragListener(this);
            //buildGoogleApiClient();
            //LocationManager mng = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            //Location location = mng.getLastKnownLocation(mng.getBestProvider(new Criteria(), false));

            //double lat = location.getLatitude();
            //double lon = location.getLongitude();

            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 13);
            //map.animateCamera(cameraUpdate);


            /*this.map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location location) {
                    //Get current location
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                    //Add a marker with an image to current location

                    //Zoom parameter is set to 14
                    CameraUpdate update = CameraUpdateFactory.newLatLng(position);

                    //Use map.animateCamera(update) if you want moving effect
                    map.moveCamera(update);

                    updateBands(location.getLatitude(), location.getLongitude());
                    //.onResume();
                    mapFragment.onResume();
                }
            });*/
            Log.e("MAP", "in onmapready");
            updateBands(51.4500, -2.5833);


        }

    public void updateBands(double latitude, double longitude) {



        String myApiKey = getString(R.string.MixRadioClientId, "");

        MixRadioClient client = new MixRadioClient(myApiKey, "gb");

        client.getArtistsAroundLocation(latitude,longitude,20,0,20,new Callback<List<Artist>>() {
            @Override
            public void success(List<Artist> artists, Response response) {
                for (int i = 0; i < artists.size(); i++) {
                    Artist artist = artists.get(i);

                    LatLng artist_location = new LatLng(artist.location.latitude,artist.location.longitude);
                    map.addMarker(new MarkerOptions()
                            .title(artist.name)
                            .position(artist_location));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(BandsAround.this,retrofitError.getResponse().getReason(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng pos = cameraPosition.target;
        Log.e("MAP", "in oncamerachange "+pos.latitude+","+pos.longitude);
        updateBands(pos.latitude, pos.longitude);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            updateBands(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 13);
            map.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

