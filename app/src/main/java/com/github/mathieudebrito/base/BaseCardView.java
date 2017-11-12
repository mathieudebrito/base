package com.github.mathieudebrito.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import javax.inject.Inject;

/**
 * @Injectable CardView
 */
public class BaseCardView extends FrameLayout {

    @Inject
    protected AppBus bus;

    protected Context context;

    public BaseCardView(Context context) {
        super(context);
        init(context);
    }

    public BaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
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
