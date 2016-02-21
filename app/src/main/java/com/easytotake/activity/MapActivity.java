package com.easytotake.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.easytotake.R;
import com.easytotake.anim.RoundedTransformation;
import com.easytotake.map.LatLngInterpolator;
import com.easytotake.map.MarkerAnimation;
import com.easytotake.map.PicassoMarker;
import com.easytotake.rest.util.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class MapActivity extends BaseDrawerActivity implements OnMapReadyCallback, LocationListener {


    public static final String ARG_LATLNG = "latlng";
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private LocationManager locationManager;
    private GoogleMap googleMap;

    private LatLng latLng;
    private Marker marker;


    public static void startMapActivity(Context startingActivity, LatLng latLng) {
        Intent intent = new Intent(startingActivity, MapActivity.class);
        intent.putExtra(ARG_LATLNG, latLng);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        latLng = getIntent().getParcelableExtra(ARG_LATLNG);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("i am coming..!");

        marker = googleMap.addMarker(markerOptions);

        PicassoMarker picassoMarker = new PicassoMarker(marker);

        Picasso.with(this)
                .load(Constants.Rest.BASE_URL + "busy.png")
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(64, 64)
                .centerCrop()
                .transform(new RoundedTransformation())
                .into(picassoMarker);

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                MarkerAnimation.animateMarkerToGB(marker, latLng, new LatLngInterpolator.Linear());
            }
        };

        handler.postDelayed(r, 5000);

    }


    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
