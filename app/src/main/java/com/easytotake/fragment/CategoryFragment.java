package com.easytotake.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easytotake.R;
import com.easytotake.adapter.CategoryAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoryFragment extends Fragment {

    @Bind(R.id.rvCategories)
    RecyclerView rvCategories;

    public static CategoryFragment instance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.view_messenger_categories, container, false);

        ButterKnife.bind(this, rootView);

        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        rvCategories.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));
        CategoryAdapter adapter = new CategoryAdapter(rvCategories, container.getContext());
        adapter.loadMore();

        rvCategories.setAdapter(adapter);

        return rootView;
    }
}
