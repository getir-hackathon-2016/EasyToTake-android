package com.easytotake.components.radiogroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

public class MaterialStateText extends TextView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };


    private boolean mChecked;

    public MaterialStateText(Context context) {
        super(context);
    }

    public MaterialStateText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialStateText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
