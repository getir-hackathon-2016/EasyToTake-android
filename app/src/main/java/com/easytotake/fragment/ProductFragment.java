package com.easytotake.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easytotake.R;

import butterknife.Bind;


public class ProductFragment extends Fragment {

    @Bind(R.id.rvProducts)
    RecyclerView rvProducts;

    public static ProductFragment instance(String parentOid) {
        ProductFragment productFragment = new ProductFragment();
        productFragment.getArguments().putString("parentOid", parentOid);
        return productFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.view_category_products, container, false);
//        ButterKnife.bind(this, rootView);
//
//        rvProducts.setHasFixedSize(true);
//        rvProducts.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        rvProducts.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
//
//        String parentOid = getArguments().getString("parentOid");
//
//        ProductAdapter adapter = new ProductAdapter(rvProducts, container.getContext(), parentOid);
//        adapter.loadMore();
//
//        rvProducts.setAdapter(adapter);

        return rootView;
    }
}
