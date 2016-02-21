package com.easytotake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.google.android.gms.maps.model.LatLng;
import com.easytotake.R;
import com.easytotake.adapter.CheckOutAdapter;

import butterknife.Bind;
import butterknife.OnClick;


public class CheckOutActivity extends BaseDrawerActivity {

    @Bind(R.id.rvCheckOuts)
    RecyclerView rvCheckOuts;


    @OnClick(R.id.btnCheckOut)
    void onCheckOut() {
        MapActivity.startMapActivity(this, new LatLng(41.082292, 29.022486));
    }

    public static void startCheckOutActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, CheckOutActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        rvCheckOuts.setHasFixedSize(true);
        rvCheckOuts.setLayoutManager(new GridLayoutManager(this, 1));
//        rvCheckOuts.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
        CheckOutAdapter adapter = new CheckOutAdapter(rvCheckOuts, this);
        adapter.loadMore();

        rvCheckOuts.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
