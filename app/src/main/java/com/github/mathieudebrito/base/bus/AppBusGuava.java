package com.github.mathieudebrito.base.bus;

import android.os.Handler;
import android.os.Looper;

import com.github.mathieudebrito.utils.Logs;
import com.google.common.eventbus.EventBus;


/**
 * The AppBus is the pipe for the events
 * If you want to use this Bus, you have to replace imports to guava @subscribe
 */
public class AppBusGuava implements AppBus {

    final Handler mHandler = new Handler(Looper.getMainLooper());

    static AppBusGuava instance = null;
    static EventBus bus = null;

    AppBusGuava() {
        bus = new EventBus();
    }

    public static AppBusGuava getInstance() {
        if (instance == null) {
            instance = new AppBusGuava();
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

        // We need to be on the mainLooper to send an event
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Logs.debug(this, event.getClass().getName() + " on MainLooper");
            bus.post(event);
        } else {
            Logs.debug(this, event.getClass().getName() + " on Handler");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }
    }
}
