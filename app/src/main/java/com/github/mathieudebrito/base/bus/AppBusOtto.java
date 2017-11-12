package com.github.mathieudebrito.base.bus;

import android.os.Handler;
import android.os.Looper;

import com.github.mathieudebrito.base.utils.Logs;
import com.squareup.otto.Bus;


/**
 * The AppBus is the pipe for the events
 *
 * @see http://square.github.io/otto/
 * https://github.com/excilys/androidannotations/wiki/OttoIntegration
 */
public class AppBusOtto implements AppBus {

    final Handler mHandler = new Handler(Looper.getMainLooper());

    static AppBusOtto instance = null;
    static Bus bus = null;

    AppBusOtto() {
        bus = new Bus();
    }

    public static AppBusOtto getInstance() {
        if (instance == null) {
            instance = new AppBusOtto();
        }
        return instance;
    }

    public void register(Object o) {
        bus.register(o);
    }

    public void unregister(Object o) {
        bus.unregister(o);
    }

    public void post(final Object event) {
        String[] split = event.getClass().getName().split("\\.");
        String className = event.getClass().getName();
        if (split.length > 0) {
            className = split[split.length - 1];
        }

        // We need to be on the mainLooper to send an event
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Logs.info(this, "MainLooper:" + className);
            bus.post(event);
        } else {
            Logs.info(this,  "Handler:" + className);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }
    }
}
