package com.github.mathieudebrito.base.backstack.events;

import android.support.v4.app.Fragment;

public class ShowRootPlusFragmentEvent {

    public final Fragment fragment;

    public String fragmentName;

    public ShowRootPlusFragmentEvent(Fragment fragment) {
        this.fragment = fragment;
        this.fragmentName = ((Object) fragment).getClass().getName();
    }

}
