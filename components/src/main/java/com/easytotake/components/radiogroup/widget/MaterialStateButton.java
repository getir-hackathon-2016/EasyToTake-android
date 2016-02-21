package com.easytotake.components.radiogroup.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.ImageView;

import com.easytotake.components.radiogroup.listener.OnStateButtonCheckedListener;

public class MaterialStateButton extends ImageView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "MaterialStateButton:";

    private boolean mChecked;

    private OnStateButtonCheckedListener mOnStateButtonCheckedListener;

    public MaterialStateButton(Context context) {
        super(context);
    }

    public MaterialStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (DEBUG) {
            Log.d(LOG_TAG, String.format("setChecked :%s", checked));
        }
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();

            if (mOnStateButtonCheckedListener != null) {
                mOnStateButtonCheckedListener.onCheckedChanged(mChecked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
        if (mOnStateButtonCheckedListener != null) {
            mOnStateButtonCheckedListener.onCheckedToggle();
        }
    }

    @Override
    public boolean performClick() {
        if (DEBUG) {
            Log.d(LOG_TAG, "performClick");
        }
        if (!isChecked()) {
            toggle();
        }
        return super.performClick();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setOnStateButtonCheckedListener(OnStateButtonCheckedListener listener) {
        this.mOnStateButtonCheckedListener = listener;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ownState = (SavedState) state;
        super.onRestoreInstanceState(ownState.getSuperState());
        setChecked(ownState.checked);
        requestLayout();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ownState = new SavedState(superState);
        ownState.checked = mChecked;
        return ownState;
    }


    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean checked;

        private SavedState(Parcel source) {
            super(source);
            checked = source.readInt() == 1;
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(checked ? 1 : 0);
        }
    }

}
