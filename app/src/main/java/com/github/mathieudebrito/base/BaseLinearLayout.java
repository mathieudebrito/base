package com.github.mathieudebrito.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import javax.inject.Inject;

/**
 * @Injectable LinearLayout
 */
public class BaseLinearLayout extends LinearLayout {

    @Inject
    protected AppBus bus;

    protected Context context;

    public BaseLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
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
