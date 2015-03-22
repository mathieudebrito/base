package com.github.mathieudebrito.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;
import com.github.mathieudebrito.utils.Toasts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import javax.inject.Inject;


/**
 * @Injectable and Eventable fragment
 */
@EFragment
public class BaseFragment extends Fragment {

    @Inject
    protected AppBus bus;
    protected Context context;
    private ProgressDialog progressDialog = null;

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

    @UiThread
    public void showWaiter() {

        hideWaiter();
        try {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void hideWaiter() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @UiThread
    public void alert(int message) {
        Toasts.error(context, message);
    }

    @UiThread
    public void inform(int message) {
        Toasts.show(context, message);
    }
}
