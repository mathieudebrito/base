package com.github.mathieudebrito.base.backstack;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import com.github.mathieudebrito.base.BaseActivity;
import com.github.mathieudebrito.base.BaseFragment;
import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.backstack.events.ClearFragmentsEvent;
import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ReplaceActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ReplaceRootFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ShowFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootPlusFragmentEvent;
import com.github.mathieudebrito.utils.KeyboardUtils;
import com.github.mathieudebrito.utils.Logs;
import com.google.common.base.Strings;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedList;


@EActivity
public class FragmentBackStackBaseActivity extends BaseActivity {

    protected String lastFragmentShown = null;
    protected LinkedList<Fragment> fragments = new LinkedList();

    public static Context thisContext; // Used for debug in CommonCallback TODO remove asap

    @OptionsItem(android.R.id.home)
    protected void up() {
        Logs.debug(this, "BackStack - up");
        int count = fragments.size();

        if (count > 1) {
            // Back button

            if (!((BaseFragment) fragments.getLast()).onBackPressed()) {
                popFragment(new PopFragmentEvent());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        this.thisContext = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logs.debug(this, "BackStack - onNewIntent");
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logs.debug(this, "BackStack - onResume");

        if (fragments.size() > 0) {
            fragments.getLast().onResume();
        }
    }

    @AfterViews
    public void bind() {
        Logs.debug(this, "BackStack - bind");

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        try {
                            Logs.debug(this, "BackStack - onBackStackChanged", "" + getSupportFragmentManager().getBackStackEntryCount());

                            updateLogoAndUp();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Bugfix on for compat lib v7 making the app crashes
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Logs.debug(this, "BackStack - onBackPressed", "" + fragments.size());
        backPressed();
    }

    public boolean backPressed() {
        if (!((BaseFragment) fragments.getLast()).onBackPressed()) {
            if (fragments.size() > 1) {
                popFragment(new PopFragmentEvent());
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    public int isInBackStack(String className) {
        int numFragment = 0;
        for (Fragment fragment : fragments) {
            if (className.equals(((Object) fragment).getClass().getName())) {
                return numFragment;
            }
            numFragment++;
        }
        return -1;
    }

    @Subscribe
    @UiThread
    public void showFragment(ShowFragmentEvent event) {
        if (event.fragmentName.equals(lastFragmentShown)) {
            Logs.debug(this, "BackStack - showFragment", "Fragment already showing");
            return;
        }

        KeyboardUtils.hide(this);

        Logs.debug(this, "BackStack - showFragment", "Fragment to show : " + Logs.getClassName(event.fragment));
        lastFragmentShown = event.fragmentName;

        fragments.addLast(event.fragment);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.base_fade_in, R.anim.base_fade_out)
                .add(R.id.fragment_container, event.fragment)
                .addToBackStack(event.fragmentName)
                .commitAllowingStateLoss();

        invalidateMenu();
    }

    @Subscribe
    @UiThread
    public void popFragment(PopFragmentEvent event) {
        Logs.debug(this, "BackStack - popFragment", "" + fragments.size());

        if (fragments.size() > 1) {

            if (!Strings.isNullOrEmpty(event.until)) {

                int frgPosition = isInBackStack(event.until);
                if (frgPosition != -1) {

                    // remove all fragments after this position
                    while (fragments.size() > 1 && fragments.size() > frgPosition) {
                        fragments.removeLast();
                    }

                    Fragment fragmentToResume = fragments.getLast();
                    fragmentToResume.onResume();

                    getSupportFragmentManager().popBackStackImmediate(event.until, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
            } else {
                fragments.pollLast();

                Fragment fragment = fragments.getLast();
                fragment.onResume();

                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        lastFragmentShown = ((Object) fragments.getLast()).getClass().getName();

        invalidateMenu();
        KeyboardUtils.hide(this);
    }

    @Subscribe
    @UiThread
    public void replaceActivity(ReplaceActivityEvent event) {
        Logs.debug(this, "BackStack - replaceActivity");
        startActivity(event.intent);
        finish();
    }

    @Subscribe
    @UiThread
    public void showActivity(ShowActivityEvent event) {
        Logs.debug(this, "BackStack - showActivity");
        startActivity(event.intent);
    }

    @Subscribe
    @UiThread
    public void showRootFragment(ShowRootFragmentEvent event) {
        Logs.debug(this, "BackStack - showRootFragment");

        if (fragments.size() > 1) {
            KeyboardUtils.hide(this);
            Logs.debug(this, "BackStack - showRootFragment", "pop all until " + getSupportFragmentManager().getBackStackEntryAt(0).getName());
            while (fragments.size() > 1) {
                fragments.removeLast();
            }
            getSupportFragmentManager().popBackStackImmediate(getSupportFragmentManager().getBackStackEntryAt(0).getId(), 0);
            fragments.get(0).onResume();
            lastFragmentShown = ((Object) fragments.getLast()).getClass().getName();
        }

        invalidateMenu();
    }

    @Subscribe
    @UiThread
    public void showRootPlusFragment(ShowRootPlusFragmentEvent event) {
        Logs.debug(this, "BackStack - showRootPlusFragment");

        showRootFragment(null);

        showFragment(new ShowFragmentEvent(event.fragment));
    }

    @Subscribe
    @UiThread
    public void clearFragments(ClearFragmentsEvent event) {
        Logs.debug(this, "BackStack - clearFragments");

        fragments.clear();
        lastFragmentShown = "";

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        KeyboardUtils.hide(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logs.debug(this, "BackStack - onActivityResult");
        Fragment fragment = fragments.getLast();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // No call for super(). Bug on API Level > 11.
        // https://code.google.com/p/android/issues/detail?id=19917
    }

    public void invalidateMenu() {

        for (int numFragment = 0; numFragment < fragments.size(); numFragment++) {
            fragments.get(numFragment).setMenuVisibility(numFragment == (fragments.size() - 1));
        }

        invalidateOptionsMenu();
    }

    public void updateLogoAndUp() {
        Logs.debug(this, "BackStack - updateLogoAndUp", "" + fragments.size());

        // First fragment is MapFragment, second is menuFragment
        // In MapFragment, we show the menu, else, we show the app icon
        int icon = -1;
        if (fragments.size() > 0) {
            Fragment fragment = fragments.getLast();
            if (fragment != null && fragment instanceof Iconable) {
                icon = ((Iconable) fragment).getIcon();
                getSupportActionBar().setIcon(icon);
            }else{
                getSupportActionBar().setIcon(null);
            }
        }else{
            getSupportActionBar().setIcon(null);
        }

        // Set title if needed
        String title = "";
        if (fragments.size() > 0) {
            Fragment fragment = fragments.getLast();
            if (fragment != null && fragment instanceof Titlable) {
                title = ((Titlable) fragment).getTitle();
            }
        }
        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}