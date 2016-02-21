package com.easytotake.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.easytotake.R;
import com.easytotake.adapter.CheckOutAdapter;
import com.easytotake.listener.RecyclerViewLongClickListener;
import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.BasketModel;
import com.easytotake.rest.model.Product;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Response;


public class CheckOutActivity extends BaseDrawerActivity implements RecyclerViewLongClickListener {

    @Bind(R.id.rvCheckOuts)
    RecyclerView rvCheckOuts;

    public static void startCheckOutActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, CheckOutActivity.class);
        startingActivity.startActivity(intent);
    }

    @OnClick(R.id.btnCheckOut)
    void onCheckOut() {
        Snackbar.make(rvCheckOuts, "Amazin your checkout is complete see you soon!", Snackbar.LENGTH_LONG).show();


        Call<BasketModel> call = RestClient.getService().checkOut("1");
        call.enqueue(new AbstractCallback<BasketModel>() {
            @Override
            public void onSuccess(Response<BasketModel> response) {
                // TODO handle may be.

            }

            @Override
            public void onFailure(Throwable t) {
                // TODO handle
            }
        });


        MapActivity.startMapActivity(this, new LatLng(41.082292, 29.022486));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        rvCheckOuts.setHasFixedSize(true);
        rvCheckOuts.setLayoutManager(new GridLayoutManager(this, 1));
//        rvCheckOuts.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
        CheckOutAdapter adapter = new CheckOutAdapter(rvCheckOuts, CheckOutActivity.this, this);
        adapter.loadMore();

        rvCheckOuts.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void recyclerViewListClickedLong(View v, final Product product) {
        updateBasket(product, "remove");

        final Snackbar snackbar = Snackbar
                .make(rvCheckOuts, "Product removing from shopping card", Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDU", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBasket(product, "add");
            }
        });

        snackbar.setActionTextColor(Color.BLUE);
        snackbar.getView().setBackgroundColor(Color.RED);
        snackbar.show();
    }
}
