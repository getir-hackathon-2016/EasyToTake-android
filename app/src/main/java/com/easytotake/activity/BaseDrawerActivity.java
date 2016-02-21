package com.easytotake.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.easytotake.BaseActivity;
import com.easytotake.R;
import com.easytotake.anim.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.BindString;

public class BaseDrawerActivity extends BaseActivity {

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    private View headerView;

    private int[] startingLocation;
    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;

    public int[] getStartingLocation() {
        return startingLocation;
    }

    public View getHeaderView() {
        return headerView;
    }

    public int getAvatarSize() {
        return avatarSize;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void setupHeader() {
        headerView = vNavigation.getHeaderView(0);
        int[] startingLocation = new int[2];
        headerView.getLocationOnScreen(startingLocation);
        startingLocation[0] += headerView.getWidth() / 2;
        this.startingLocation = startingLocation;

        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });

        Picasso.with(this)
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        Toast.makeText(this, "Go to user profile", Toast.LENGTH_LONG).show();
    }
}
