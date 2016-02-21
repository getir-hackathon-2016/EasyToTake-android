package com.easytotake.activity;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.easytotake.R;
import com.easytotake.anim.CircleTransformation;
import com.easytotake.map.MapItemRendered;
import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.Messenger;
import com.easytotake.rest.util.Constants;
import com.easytotake.util.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import retrofit.Call;
import retrofit.Response;

public class MainActivity extends BaseDrawerActivity implements OnMapReadyCallback, LocationListener {

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    @Bind(R.id.mapRootLayout)
    public View mapRootLayout;
    @Bind(R.id.root)
    public View root;
    @BindColor(R.color.colorPrimary)
    public int colorPrimary;
    private boolean pendingIntroAnimation;
    private GoogleMap googleMap;
    private LocationManager locationManager;

    private ClusterManager<Messenger> mClusterManager;
    private List<Messenger> items;
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        mapRootLayout.setVisibility(View.INVISIBLE);
        int actionbarSize = Utils.dpToPx(56);
        ViewHelper.setTranslationY(getToolbar(), -actionbarSize);
        ViewHelper.setTranslationY(getIvLogo(), -actionbarSize);

        ViewPropertyAnimator.animate(getToolbar())
                .translationY(0)
                .setDuration(300)
                .setStartDelay(300);
        ViewPropertyAnimator.animate(getIvLogo())
                .translationY(0)
                .setDuration(300)
                .setStartDelay(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                }).start();

    }

    private void startContentAnimation() {
        ViewHelper.setTranslationY(mapRootLayout, -mapRootLayout.getHeight());
        mapRootLayout.setVisibility(View.VISIBLE);
        ViewPropertyAnimator.animate(mapRootLayout).translationY(0).setDuration(300).setInterpolator(Utils.INTERPOLATOR);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMapIfNeeded();
        if (this.location != null) {
            onLocationChanged(this.location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (googleMap == null) {
            this.location = location;
        } else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);
            locationManager.removeUpdates(this);
        }
    }

    private void setUpMapIfNeeded() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mClusterManager = new ClusterManager<Messenger>(this, googleMap);
        mClusterManager.setRenderer(new MapItemRendered(this, googleMap, mClusterManager));

        googleMap.setOnCameraChangeListener(mClusterManager);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                int size = Integer.parseInt(marker.getTitle());
                MessengerDetailActivity.startMessengerDetailActivity(MainActivity.this, items.get(size));
                overridePendingTransition(0, 0);
            }
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                try {
                    View v = getLayoutInflater().inflate(R.layout.item_marker_view, null);
                    int size = Integer.parseInt(marker.getTitle());

                    final Messenger messenger = items.get(size);

                    ImageView picture = (ImageView) v.findViewById(R.id.picture);

                    Picasso.with(MainActivity.this)
                            .load(Constants.Rest.BASE_URL + messenger.getPicture())
                            .placeholder(R.drawable.img_circle_placeholder)
                            .resize(getAvatarSize(), getAvatarSize())
                            .centerCrop()
                            .transform(new CircleTransformation())
                            .into(picture);

                    TextView name = (TextView) v.findViewById(R.id.name);
                    name.setText(messenger.getName());

                    TextView distance = (TextView) v.findViewById(R.id.distance);
                    distance.setText(messenger.getDistance());


                    return v;

                } catch (Exception e) {
                    return null;
                }
            }
        });
        getMarkers();

    }

    private void getMarkers() {
        Call<List<Messenger>> call = RestClient.getService().messengers();
        call.enqueue(new AbstractCallback<List<Messenger>>() {
            @Override
            public void onSuccess(Response<List<Messenger>> response) {
                items = response.body();
                mClusterManager.addItems(items);
            }

            @Override
            public void onFailure(Throwable t) {
                final Snackbar snackbar = Snackbar
                        .make(root, t.getMessage(), Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        getMarkers();
                    }
                });

                snackbar.setActionTextColor(Color.RED);
                snackbar.getView().setBackgroundColor(colorPrimary);
                snackbar.show();
            }
        });
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
