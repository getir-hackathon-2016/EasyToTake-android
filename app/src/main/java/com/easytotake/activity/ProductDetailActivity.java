package com.easytotake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytotake.R;
import com.easytotake.adapter.DetailAdapter;
import com.easytotake.anim.CircleTransformation;
import com.easytotake.components.view.RevealBackgroundView;
import com.easytotake.fragment.ProductAboutFragment;
import com.easytotake.rest.model.Product;
import com.easytotake.rest.util.Constants;
import com.easytotake.util.Utils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindDimen;

public class ProductDetailActivity extends BaseDrawerActivity implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_PRODUCT = "product";

    @BindDimen(R.dimen.messenger_profile_avatar_size)
    int avatarSize;

    @Bind(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;

    @Bind(R.id.tlMessengerProfileTabs)
    TabLayout tlMessengerProfileTabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.ivMessengerProfilePhoto)
    ImageView ivMessengerProfilePhoto;

    @Bind(R.id.vMessengerDetails)
    View vMessengerDetails;

    @Bind(R.id.vMessengerProfileRoot)
    View vMessengerProfileRoot;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.contentRoot)
    View contentRoot;

    @Bind(R.id.btnAddBasket)
    FloatingActionButton btnAddBasket;

    private Product product;

    public static void startProductDetailActivity(Context startingActivity, Product product) {
        Intent intent = new Intent(startingActivity, ProductDetailActivity.class);
        intent.putExtra(ARG_PRODUCT, product);
        startingActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setPlayAnimation(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_profile);
        this.product = (Product) getIntent().getExtras().get(ARG_PRODUCT);

        Picasso.with(this)
                .load(Constants.Rest.BASE_URL + this.product.getPicture())
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMessengerProfilePhoto);


        setupViewPager(viewPager);

        tlMessengerProfileTabs.setupWithViewPager(viewPager);

        setupRevealBackground(savedInstanceState);
        setupMessengerProfile();
        setupTabIcons();
        btnAddBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBasket(product, "add");
                Snackbar.make(contentRoot, "Adding", Snackbar.LENGTH_SHORT).show();
            }
        });
        btnAddBasket.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(contentRoot, "Opening shopping card page", Snackbar.LENGTH_SHORT).show();
                CheckOutActivity.startCheckOutActivity(ProductDetailActivity.this);
                return true;
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(2);
        DetailAdapter adapter = new DetailAdapter(getSupportFragmentManager());
        adapter.addFragment(ProductAboutFragment.instance(), "ABOUT");
        adapter.addFragment(ProductAboutFragment.instance(), "COMMENTS");
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        tlMessengerProfileTabs.getTabAt(0).setIcon(R.mipmap.ic_grid_on_white);
        tlMessengerProfileTabs.getTabAt(1).setIcon(R.mipmap.ic_list_white);
    }

    private void setupMessengerProfile() {
        name.setText(product.getName());
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getStartingLocation();
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            tlMessengerProfileTabs.setVisibility(View.VISIBLE);
            vMessengerProfileRoot.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            animateUserProfileOptions();
            animateUserProfileHeader();
            animateViewPager();
        } else {
            tlMessengerProfileTabs.setVisibility(View.INVISIBLE);
            vMessengerProfileRoot.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
        }
    }

//    @Override
//    public void onBackPressed() {
//        vRevealBackground.setVisibility(View.GONE);
//        ViewCompat.setElevation(getToolbar(), 0);
//        ViewPropertyAnimator.animate(contentRoot)
//                .translationY(Utils.getScreenHeight(this))
//                .setDuration(300)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        ProductDetailActivity.super.onBackPressed();
//                        overridePendingTransition(0, 0);
//                    }
//                })
//                .start();
//    }

    private void animateUserProfileOptions() {
        ViewHelper.setTranslationY(tlMessengerProfileTabs, -tlMessengerProfileTabs.getHeight());
        ViewPropertyAnimator.animate(tlMessengerProfileTabs).translationY(0).setDuration(300).setStartDelay(300).setInterpolator(Utils.INTERPOLATOR);
    }

    private void animateViewPager() {
        ViewHelper.setTranslationY(viewPager, -viewPager.getHeight());
        ViewPropertyAnimator.animate(viewPager).translationY(0).setDuration(500).setInterpolator(Utils.INTERPOLATOR);
    }

    private void animateUserProfileHeader() {
        ViewHelper.setTranslationY(vMessengerProfileRoot, -vMessengerProfileRoot.getHeight());
        ViewHelper.setTranslationY(ivMessengerProfilePhoto, -ivMessengerProfilePhoto.getHeight());
        ViewHelper.setTranslationY(vMessengerDetails, -vMessengerDetails.getHeight());
        ViewPropertyAnimator.animate(vMessengerProfileRoot).translationY(0).setDuration(300).setInterpolator(Utils.INTERPOLATOR);
        ViewPropertyAnimator.animate(ivMessengerProfilePhoto).translationY(0).setDuration(300).setStartDelay(100).setInterpolator(Utils.INTERPOLATOR);
        ViewPropertyAnimator.animate(vMessengerDetails).translationY(0).setDuration(300).setStartDelay(200).setInterpolator(Utils.INTERPOLATOR).start();
    }
}
