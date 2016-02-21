package com.easytotake.components.edittext.showhidepassword;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.easytotake.components.R;
import com.rengwuxian.materialedittext.MaterialEditText;


public class ShowHidePasswordEditText extends MaterialEditText implements View.OnTouchListener {

    private boolean isShowingPassword = false;
    private Drawable drawableRight;
    private Rect bounds;
    private int fuzz = 20;
    @DrawableRes
    private int visiblityIndicatorShow = R.drawable.ic_visibility_grey_900_24dp;
    @DrawableRes
    private int visiblityIndicatorHide = R.drawable.ic_visibility_off_grey_900_24dp;

    public ShowHidePasswordEditText(Context context) {
        super(context);
        init(null);
    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attrsArray =
                    getContext().obtainStyledAttributes(attrs, R.styleable.ShowHidePasswordEditText);

            visiblityIndicatorShow = attrsArray.getResourceId(R.styleable.ShowHidePasswordEditText_drawable_show, visiblityIndicatorShow);
            visiblityIndicatorHide = attrsArray.getResourceId(R.styleable.ShowHidePasswordEditText_drawable_hide, visiblityIndicatorHide);

            attrsArray.recycle();
        }

        isShowingPassword = false;
        setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD, true);


        this.setOnTouchListener(this);
        showPasswordVisibilityIndicator(true);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (right != null) {
            //keep a reference to the right drawable so later on touch we can check if touch is on the drawable
            drawableRight = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && drawableRight != null) {

            final int x = (int) event.getX();
            final int y = (int) event.getY();
            bounds = drawableRight.getBounds();
            if (x >= (v.getRight() - bounds.width() - fuzz) && x <= (v.getRight() - v.getPaddingRight() + fuzz)
                    && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
                this.togglePasswordVisability();
                event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
            }
        }
        return super.onTouchEvent(event);
    }

    private void showPasswordVisibilityIndicator(boolean show) {
        if (show) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, isShowingPassword ?
                    getResources().getDrawable(visiblityIndicatorHide) :
                    getResources().getDrawable(visiblityIndicatorShow)
                    , null);
        } else {
            setCompoundDrawables(null, null, null, null);
        }
    }

    private void togglePasswordVisability() {
        if (isShowingPassword) {
            setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD, true);
        } else {
            setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD, true);
        }
        isShowingPassword = !isShowingPassword;
        showPasswordVisibilityIndicator(true);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        bounds = null;
        super.finalize();
    }


    private void setInputType(int inputType, boolean keepState) {
        int selectionStart = -1;
        int selectionEnd = -1;
        if (keepState) {
            selectionStart = getSelectionStart();
            selectionEnd = getSelectionEnd();
        }
        setInputType(inputType);
        if (keepState) {
            setSelection(selectionStart, selectionEnd);
        }
    }


    public
    @DrawableRes
    int getVisiblityIndicatorShow() {
        return visiblityIndicatorShow;
    }

    public void setVisiblityIndicatorShow(@DrawableRes int visiblityIndicatorShow) {
        this.visiblityIndicatorShow = visiblityIndicatorShow;
    }

    public
    @DrawableRes
    int getVisiblityIndicatorHide() {
        return visiblityIndicatorHide;
    }

    public void setVisiblityIndicatorHide(@DrawableRes int visiblityIndicatorHide) {
        this.visiblityIndicatorHide = visiblityIndicatorHide;
    }

    /**
     * @return true if the password is visable | false if hidden
     */
    public boolean isShowingPassword() {
        return isShowingPassword;
    }

}