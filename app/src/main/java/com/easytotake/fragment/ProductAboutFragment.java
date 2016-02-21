package com.easytotake.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easytotake.R;


public class ProductAboutFragment extends Fragment {
    
    public static ProductAboutFragment instance() {
        ProductAboutFragment productAboutFragment = new ProductAboutFragment();
        return productAboutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_product_info, container, false);

        return rootView;
    }
}
