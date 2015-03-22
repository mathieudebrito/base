package com.github.mathieudebrito.base;

import android.app.IntentService;
import android.content.Context;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

import javax.inject.Inject;

/**
 * @Injectable and Eventable IntentService
 */
public abstract class BaseIntentService extends IntentService {

    @Inject
    protected AppBus bus;

    protected Context context;

    public BaseIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GraphRetriever.from(this).inject(this);
        context = getApplicationContext();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
