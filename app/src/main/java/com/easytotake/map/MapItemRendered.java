package com.easytotake.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.easytotake.R;
import com.easytotake.anim.RoundedTransformation;
import com.easytotake.rest.model.Messenger;
import com.easytotake.rest.util.Constants;
import com.squareup.picasso.Picasso;


public class MapItemRendered extends DefaultClusterRenderer<Messenger> {

    private Context context;

    public MapItemRendered(Context context, GoogleMap map, ClusterManager<Messenger> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(Messenger messenger, MarkerOptions markerOptions) {

        // TODO change another good way
        markerOptions.title(String.valueOf(messenger.getOid()));
        super.onBeforeClusterItemRendered(messenger, markerOptions);
    }

    @Override
    protected void onClusterItemRendered(Messenger messenger, Marker marker) {
        super.onClusterItemRendered(messenger, marker);
        loadIconFromApi(messenger, marker);
    }

    private void loadIconFromApi(Messenger messenger, Marker marker) {
        PicassoMarker picassoMarker = new PicassoMarker(marker);
        Picasso.with(context)
                .load(Constants.Rest.BASE_URL + messenger.getPicture())
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(64, 64)
                .centerCrop()
                .transform(new RoundedTransformation())
                .into(picassoMarker);
    }
}
