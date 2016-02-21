package com.easytotake.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.easytotake.R;
import com.easytotake.drawable.BadgeDrawable;

public class Utils {
    public static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    public static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    public static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    public static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            screenHeight = metrics.heightPixels;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void setBadgeCount(LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable();
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
