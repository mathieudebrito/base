package com.github.mathieudebrito.base;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import javax.inject.Inject;

/**
 * @Injectable BroadcastReceiver
 */
public class BaseReceiver extends WakefulBroadcastReceiver {

    @Inject
    protected AppBus bus;

    @Override
    public void onReceive(Context context, Intent intent) {
        GraphRetriever.from(context).inject(this);
    }

}
