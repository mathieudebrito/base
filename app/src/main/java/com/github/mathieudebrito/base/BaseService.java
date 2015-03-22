package com.github.mathieudebrito.base;

import android.app.Service;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import javax.inject.Inject;

/**
 * @Injectable and Eventable Service
 */
public abstract class BaseService extends Service {

    @Inject
    protected AppBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        GraphRetriever.from(this).inject(this);
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
