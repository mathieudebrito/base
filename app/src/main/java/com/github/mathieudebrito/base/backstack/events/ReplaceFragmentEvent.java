package com.github.mathieudebrito.base.backstack.events;


import android.support.v4.app.Fragment;

public class ReplaceFragmentEvent {

    public final Fragment fragment;

    public final boolean addToBackstack;

    public String fragmentName;

    public ReplaceFragmentEvent(Fragment fragment) {
        this(fragment, true);
    }

    public ReplaceFragmentEvent(Fragment fragment, boolean addToBackstack) {
        this.fragment = fragment;
        this.addToBackstack = addToBackstack;
        this.fragmentName = ((Object) fragment).getClass().getName();
    }
}
