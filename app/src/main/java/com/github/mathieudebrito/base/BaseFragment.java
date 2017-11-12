package com.github.mathieudebrito.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;


/**
 * @Injectable and Eventable fragment
 */
@EFragment
public class BaseFragment extends Fragment {

    @Inject
    protected AppBus bus;
    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        GraphRetriever.from(getActivity()).inject(this);
        bus.register(this);
    }

    @AfterViews
    public void baseAfterView() {

        // Preempt the click on a fragment behind this one
        getView().setClickable(true);
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

    public void showActionBarWaiter() {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
    }

    public void hideActionBarWaiter() {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    /**
     * @return true if the fragment managed the back pressed action,
     * false if parents need to care care about the back pressed action
     */
    public boolean onBackPressed() {
        return false;
    }

    protected void popFragment() {
        bus.post(new PopFragmentEvent());
    }

}
