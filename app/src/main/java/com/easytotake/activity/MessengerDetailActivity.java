package com.easytotake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytotake.R;
import com.easytotake.adapter.DetailAdapter;
import com.easytotake.anim.CircleTransformation;
import com.easytotake.components.view.RevealBackgroundView;
import com.easytotake.fragment.CategoryFragment;
import com.easytotake.fragment.TopProductFragment;
import com.easytotake.rest.model.Messenger;
import com.easytotake.rest.util.Constants;
import com.easytotake.util.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindDimen;

public class MessengerDetailActivity extends BaseDrawerActivity implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_MESSENGER = "messenger";

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

    @Bind(R.id.vMessengerStats)
    View vMessengerStats;

    @Bind(R.id.vMessengerProfileRoot)
    View vMessengerProfileRoot;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.distance)
    TextView distance;

    @Bind(R.id.contentRoot)
    View contentRoot;

    private Messenger messenger;

    public static void startMessengerDetailActivity(Activity startingActivity, Messenger messenger) {
        Intent intent = new Intent(startingActivity, MessengerDetailActivity.class);
        intent.putExtra(ARG_MESSENGER, messenger);
        startingActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setPlayAnimation(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeger_profile);
        this.messenger = (Messenger) getIntent().getExtras().get(ARG_MESSENGER);


        Picasso.with(this)
                .load(Constants.Rest.BASE_URL + this.messenger.getPicture())
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
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(2);
        DetailAdapter adapter = new DetailAdapter(getSupportFragmentManager());
        adapter.addFragment(CategoryFragment.instance(), "CATEGORIES");

        adapter.addFragment(TopProductFragment.instance(), "TOP SALES");
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        tlMessengerProfileTabs.getTabAt(0).setIcon(R.mipmap.ic_grid_on_white);
        tlMessengerProfileTabs.getTabAt(1).setIcon(R.mipmap.ic_list_white);
    }

    private void setupMessengerProfile() {
        name.setText(messenger.getName());
        distance.setText(messenger.getDistance());
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

    @Override
    public void onBackPressed() {
        vRevealBackground.setVisibility(View.GONE);
        ViewCompat.setElevation(getToolbar(), 0);
        ViewPropertyAnimator.animate(contentRoot)
                .translationY(Utils.getScreenHeight(this))
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        MessengerDetailActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

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
        ViewHelper.setAlpha(vMessengerStats, 0);
        ViewPropertyAnimator.animate(vMessengerProfileRoot).translationY(0).setDuration(300).setInterpolator(Utils.INTERPOLATOR);
        ViewPropertyAnimator.animate(ivMessengerProfilePhoto).translationY(0).setDuration(300).setStartDelay(100).setInterpolator(Utils.INTERPOLATOR);
        ViewPropertyAnimator.animate(vMessengerDetails).translationY(0).setDuration(300).setStartDelay(200).setInterpolator(Utils.INTERPOLATOR);
        ViewPropertyAnimator.animate(vMessengerStats).alpha(1).setDuration(200).setStartDelay(400).setInterpolator(Utils.INTERPOLATOR).start();
    }
}
