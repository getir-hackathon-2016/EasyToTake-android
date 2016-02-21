package com.easytotake.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easytotake.R;
import com.easytotake.adapter.ProductTopAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TopProductFragment extends Fragment {

    @Bind(R.id.rvProducts)
    RecyclerView rvProducts;

    public static TopProductFragment instance() {
        TopProductFragment productFragment = new TopProductFragment();
        return productFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.view_category_products, container, false);
        ButterKnife.bind(this, rootView);

        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        rvProducts.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));

        ProductTopAdapter adapter = new ProductTopAdapter(rvProducts, container.getContext());
        adapter.loadMore();

        rvProducts.setAdapter(adapter);

        return rootView;
    }
}
