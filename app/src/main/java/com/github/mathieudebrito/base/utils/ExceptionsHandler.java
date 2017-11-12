package com.github.mathieudebrito.base.utils;


import com.github.mathieudebrito.base.BuildConfig;

public class ExceptionsHandler {

    public static void handle(Throwable e) {
        if (BuildConfig.DEBUG) {

            // Local
            Logs.error(ExceptionsHandler.class, e.getMessage());
        }
        // Crashlytics
        e.printStackTrace();
    }
}
