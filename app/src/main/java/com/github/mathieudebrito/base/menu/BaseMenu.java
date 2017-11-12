package com.github.mathieudebrito.base.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.mathieudebrito.base.BaseLinearLayout;
import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.menu.events.OpenDrawerMenuEvent;
import com.github.mathieudebrito.base.utils.AppUtils;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * If you want to create Up and drawer icons :
 * http://romannurik.github.io/AndroidAssetStudio/icons-nav-drawer-indicator.html
 */
@EViewGroup(resName="drw_menu")
public class BaseMenu extends BaseLinearLayout {

    @Inject
    protected AppBus bus;

    @ViewById
    protected TextView txtVersionName;

    public BaseMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void init() {
        if (!isInEditMode()) {
            txtVersionName.setText("v" + AppUtils.getVersion(context));
        }
    }

    @Subscribe
    @UiThread
    public void updateMenu(OpenDrawerMenuEvent event) {
        init();
    }
}