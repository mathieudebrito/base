package com.github.mathieudebrito.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import javax.inject.Inject;

/**
 * @Injectable and Eventable fragment
 */
@EFragment
public class BasePreferenceFragment extends PreferenceFragmentCompat {

    @Inject
    protected AppBus bus;
    protected Context context;
    protected Dialog loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        GraphRetriever.from(getActivity()).inject(this);
        bus.register(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @AfterViews
    public void baseAfterView() {

        // Preempt the click on a fragment behind this one
        if (getView() != null) {
            getView().setClickable(true);
        }
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * @return true if the fragment managed the back pressed action,
     * false if mainCategories need to care care about the back pressed action
     */
    public boolean onBackPressed() {
        return false;
    }

    protected void popFragment() {
        bus.post(new PopFragmentEvent());
    }

}
