package com.github.mathieudebrito.base.backstack;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mathieudebrito.base.BaseFragment;
import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.backstack.events.ClearFragmentsEvent;
import com.github.mathieudebrito.base.backstack.events.PopFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ReplaceActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ShowActivityEvent;
import com.github.mathieudebrito.base.backstack.events.ShowFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootFragmentEvent;
import com.github.mathieudebrito.base.backstack.events.ShowRootPlusFragmentEvent;
import com.github.mathieudebrito.utils.KeyboardUtils;
import com.github.mathieudebrito.utils.Logs;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(resName = "base_act_main")
public class DrawerFragmentBackStackBaseActivity extends FragmentBackStackBaseActivity {

    @ViewById(resName = "drwMain")
    protected DrawerLayout drwMain;
    protected ActionBarDrawerToggle drwToggle;
    @ViewById
    protected Toolbar acbToolbar;
    @ViewById(resName = "layMenu")
    protected RelativeLayout layMenu;

    @OptionsItem(android.R.id.home)
    protected void up() {
        int count = fragments.size();

        if (count > 1) {
            // Back button

            if (!((BaseFragment) fragments.getLast()).onBackPressed()) {
                popFragment(new PopFragmentEvent());
            }
        } else {
            if (drwMain.isDrawerOpen(layMenu)) {
                drwMain.closeDrawer(layMenu);
            } else {
                KeyboardUtils.hide(this);
                drwMain.openDrawer(layMenu);
            }
        }
    }

    @AfterViews
    @Override
    public void bind() {
        Logs.debug(this, "DrawerBackStack - bind");

        if (acbToolbar == null) {
            throw new RuntimeException("DrawerBackStack - Toolbar is null");
        }
        if (drwMain == null) {
            throw new RuntimeException("DrawerBackStack - Drawer is null");
        }

        setSupportActionBar(acbToolbar);

        acbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
                drwToggle.syncState();
            }
        });

        drwToggle = new ActionBarDrawerToggle(this, drwMain, acbToolbar, R.string.abc_action_menu_overflow_description, R.string.abc_action_bar_up_description) {

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                invalidateMenu();
                drwToggle.syncState();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateMenu();
            }
        };
        drwToggle.setDrawerIndicatorEnabled(true);

        // Set the drawer toggle as the DrawerListener
        drwMain.setDrawerListener(drwToggle);

        super.bind();
    }

    @Override
    public void onBackPressed() {
        Logs.debug(this, "DrawerBackStack - onBackPressed");

        if (!super.backPressed()) {
            if (drwMain.isDrawerOpen(layMenu)) {
                drwMain.closeDrawer(layMenu);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //drwToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drwToggle.onConfigurationChanged(newConfig);
    }

    @Subscribe
    @UiThread
    @Override
    public void showFragment(ShowFragmentEvent event) {
        Logs.debug(this, "DrawerBackStack - showFragment", Logs.getClassName(event.fragment));

        closeDrawer();
        super.showFragment(event);
    }

    @Subscribe
    @UiThread
    @Override
    public void popFragment(PopFragmentEvent event) {
        Logs.debug(this, "DrawerBackStack - popFragment", "" + fragments.size());

        closeDrawer();
        super.popFragment(event);
    }

    @Subscribe
    @UiThread
    public void replaceActivity(ReplaceActivityEvent event) {
        Logs.debug(this, "DrawerBackStack - replaceActivity");

        closeDrawer();
        super.replaceActivity(event);
    }

    @Subscribe
    @UiThread
    @Override
    public void showActivity(ShowActivityEvent event) {
        Logs.debug(this, "DrawerBackStack - showActivity");

        closeDrawer();
        super.showActivity(event);
    }

    @Subscribe
    @UiThread
    @Override
    public void showRootFragment(ShowRootFragmentEvent event) {
        Logs.debug(this, "DrawerBackStack - showRootFragment");

        closeDrawer();
        super.showRootFragment(event);
    }

    @Subscribe
    @UiThread
    @Override
    public void showRootPlusFragment(ShowRootPlusFragmentEvent event) {
        Logs.debug(this, "DrawerBackStack - showRootPlusFragment");

        closeDrawer();
        super.showRootPlusFragment(event);
    }


    @Subscribe
    @UiThread
    public void clearFragments(ClearFragmentsEvent event) {
        Logs.debug(this, "DrawerBackStack - clearFragments");

        closeDrawer();
        super.clearFragments(event);
    }

    public void closeDrawer() {
        if (drwMain.isDrawerOpen(layMenu)) {
            drwMain.closeDrawer(layMenu);
        }
    }

    @Override
    public void updateLogoAndUp() {
        Logs.debug(this, "DrawerBackStack - updateLogoAndUp", "" + fragments.size());

        super.updateLogoAndUp();

        boolean show = fragments.size() > 1;
        if (show) {
            Logs.debug(this, "DrawerBackStack - updateLogoAndUp", "Showing up");
            drwToggle.setDrawerIndicatorEnabled(false);
        } else {
            Logs.debug(this, "DrawerBackStack - updateLogoAndUp", "Showing Menu");
            drwToggle.setDrawerIndicatorEnabled(true);
        }

        drwToggle.syncState();
        drwToggle.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        drwToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up();
            }
        });

    }

}