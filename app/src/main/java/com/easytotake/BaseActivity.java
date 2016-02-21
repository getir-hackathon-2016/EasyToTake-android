package com.easytotake;

import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.easytotake.activity.CheckOutActivity;
import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.BasketModel;
import com.easytotake.rest.model.Product;
import com.easytotake.util.PreferencesManager;
import com.easytotake.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by hasanmumin on 19/02/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    private MenuItem basketMenuItem;
    private boolean playAnimation = true;

    public MenuItem getBasketMenuItem() {
        return basketMenuItem;
    }

    public boolean isPlayAnimation() {
        return playAnimation;
    }

    public void setPlayAnimation(boolean playAnimation) {
        this.playAnimation = playAnimation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onResume() {
        updateBasket(null, null);
        super.onResume();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        ButterKnife.bind(this);
        setupToolbar();
        if (isPlayAnimation()) {
            setupAnimation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        basketMenuItem = menu.findItem(R.id.action_basket);
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_basket);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(icon, PreferencesManager.getInstance(getApplicationContext()).getShopping());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_basket) {
            CheckOutActivity.startCheckOutActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateBasket(Product product, String type) {
        if (type != null) {
            if (type.equals("add")) {
                PreferencesManager.getInstance(getApplicationContext()).increment();
                // increment
            } else if (type.equals("remove")) {
                // decrement
                PreferencesManager.getInstance(getApplicationContext()).decremenet();
            }
            product.setParentOid("1");

            Call<BasketModel> call = RestClient.getService().updateShoppingCard(product);
            call.enqueue(new AbstractCallback<BasketModel>() {
                @Override
                public void onSuccess(Response<BasketModel> response) {
                    // TODO when success check again basket count size
                    BasketModel basketModel = response.body();
                    if (basketModel.isSuccess()) {
                    } else {
                        // TODO handle exception
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO handle exception
                    System.out.println(t.getMessage());
                }
            });
        }
        supportInvalidateOptionsMenu();
    }

    protected void setupAnimation() {
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }
}
