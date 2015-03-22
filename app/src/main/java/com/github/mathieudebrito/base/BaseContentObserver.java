package com.github.mathieudebrito.base;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.github.mathieudebrito.base.injects.GraphRetriever;

/**
 * @Injectable ContentObserver
 */
public class BaseContentObserver extends ContentObserver {

    public BaseContentObserver(Context context, Handler handler) {
        super(handler);
        GraphRetriever.from(context).inject(this);
    }
}
