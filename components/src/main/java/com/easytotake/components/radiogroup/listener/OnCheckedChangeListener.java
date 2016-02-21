package com.easytotake.components.radiogroup.listener;

import com.easytotake.components.radiogroup.MaterialRadioGroup;

public interface OnCheckedChangeListener {

    /**
     * <p>Called when the checked radio button has changed. When the
     * selection is cleared, checkedId is -1.</p>
     *
     * @param group     the group in which the checked radio button has changed
     * @param checkedId the unique identifier of the newly checked radio button
     */
    public void onCheckedChanged(MaterialRadioGroup group, int checkedId);
}
