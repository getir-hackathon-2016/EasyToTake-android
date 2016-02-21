package com.easytotake.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.easytotake.R;
import com.easytotake.adapter.ProductAdapter;
import com.easytotake.listener.RecyclerViewLongClickListener;
import com.easytotake.rest.model.Product;

import butterknife.Bind;
import butterknife.OnClick;

public class CategoryDetailActivity extends BaseDrawerActivity implements RecyclerViewLongClickListener {

    private static final String ARG_PARENT_OID = "parentOid";
    @Bind(R.id.rvCategories)
    RecyclerView rvCategories;

    public static void startCategoryDetailActivity(Context startingActivity, String parentOid) {
        Intent intent = new Intent(startingActivity, CategoryDetailActivity.class);
        intent.putExtra(ARG_PARENT_OID, parentOid);
        startingActivity.startActivity(intent);
    }

    @OnClick(R.id.btnReadQuarCode)
    void onbtnReadQuarCode() {
        ScannerViewActivity.startScannerActivity(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
//        rvCategories.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));

        String parentOid = getIntent().getStringExtra(ARG_PARENT_OID);

        ProductAdapter adapter = new ProductAdapter(rvCategories, this, parentOid, this);
        adapter.loadMore();

        rvCategories.setAdapter(adapter);

        Snackbar.make(rvCategories, "Long click for direct add to shopping cart", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void recyclerViewListClickedLong(View v, final Product product) {
        updateBasket(product, "add");

        final Snackbar snackbar = Snackbar
                .make(rvCategories, "Product adding to shopping card", Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDU", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBasket(product, "remove");
            }
        });

        snackbar.setActionTextColor(Color.BLUE);
        snackbar.getView().setBackgroundColor(Color.RED);
        snackbar.show();
    }
}
