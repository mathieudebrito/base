package com.github.mathieudebrito.base;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import org.androidannotations.annotations.EViewGroup;

import javax.inject.Inject;


/**
 * @Injectable RelativeLayout
 */
@EViewGroup
public class BaseRelativeLayout extends RelativeLayout {

    @Inject
    protected AppBus bus;

    protected Context context;

    public BaseRelativeLayout(Context context) {
        super(context);
        this.context = context;
        if (!isInEditMode()) {
            GraphRetriever.from(context).inject(this);
        }
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (!isInEditMode()) {
            GraphRetriever.from(context).inject(this);
        }
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        if (!isInEditMode()) {
            GraphRetriever.from(context).inject(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            bus.register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()) {
            bus.unregister(this);
        }
        super.onDetachedFromWindow();
    }

}
