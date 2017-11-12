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
import com.github.mathieudebrito.base.BasePreferenceFragment;
import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.backstack.events.ClearFragmentsEvent;
import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ReplaceActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ReplaceRootFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ShowFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootPlusFragmentEvent;
import com.github.mathieudebrito.base.utils.KeyboardUtils;
import com.github.mathieudebrito.base.utils.Logs;
import com.github.mathieudebrito.base.utils.Objects;
import com.github.mathieudebrito.base.utils.Strings;
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
        Logs.method(this);
        int count = fragments.size();

        if (count > 1) {
            // Back button

            if (!lastFragmentOnBackPressed()) {
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
        Logs.method(this);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logs.method(this);

        if (fragments.size() > 0) {
            fragments.getLast().onResume();
        }
    }

    @AfterViews
    public void bind() {
        Logs.method(this);

        initActionBar();
        initFragmentManager();
    }

    public void initActionBar() {
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void initFragmentManager() {
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        try {
                            Logs.debug(this, "fragments: " + getSupportFragmentManager().getBackStackEntryCount());

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
        Logs.debug(this, "fragments: " + fragments.size());
        backPressed();
    }

    public boolean backPressed() {
        Logs.method(this, "fragments: " + fragments.size());

        try {
            if (!lastFragmentOnBackPressed()) {
                if (fragments.size() > 1) {
                    popFragment(new PopFragmentEvent());
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {

        }return false;
    }

    public boolean lastFragmentOnBackPressed() {
        Fragment lastFragment = fragments.getLast();
        if (lastFragment instanceof BasePreferenceFragment) {
            return ((BasePreferenceFragment) lastFragment).onBackPressed();
        } else {
            return ((BaseFragment) lastFragment).onBackPressed();
        }
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
            Logs.warn(this, "Fragment already showing");
            return;
        }

        KeyboardUtils.hide(this);

        Logs.method(this, "Fragment to show : " + Objects.name(event.fragment));
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
        Logs.debug(this, "" + fragments.size());

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
        Logs.method(this);
        startActivity(event.intent);
        finish();
    }

    @Subscribe
    @UiThread
    public void showActivity(ShowActivityEvent event) {
        Logs.method(this);
        startActivity(event.intent);
    }

    @Subscribe
    @UiThread
    public void showRootFragment(ShowRootFragmentEvent event) {
        Logs.method(this);

        if (fragments.size() > 1) {
            KeyboardUtils.hide(this);
            Logs.debug(this, "pop all until " + getSupportFragmentManager().getBackStackEntryAt(0).getName());
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
        Logs.method(this);

        showRootFragment(null);

        showFragment(new ShowFragmentEvent(event.fragment));
    }

    @Subscribe
    @UiThread
    public void clearFragments(ClearFragmentsEvent event) {
        Logs.method(this);

        fragments.clear();
        lastFragmentShown = "";

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        KeyboardUtils.hide(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logs.method(this);
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
        Logs.debug(this, "fragments: " + fragments.size());

        // First fragment is MapFragment, second is menuFragment
        // In MapFragment, we show the menu, else, we show the app icon
        int icon = -1;
        if (fragments.size() > 0) {
            Fragment fragment = fragments.getLast();
            if (fragment != null && fragment instanceof Iconable) {
                icon = ((Iconable) fragment).getIcon();
                getSupportActionBar().setIcon(icon);
            } else {
                getSupportActionBar().setIcon(null);
            }
        } else {
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