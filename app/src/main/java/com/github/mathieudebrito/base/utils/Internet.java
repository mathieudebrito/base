package com.github.mathieudebrito.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {
    public static boolean isAvailable(Context context) {
        boolean status = false;
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network_info = connMgr.getActiveNetworkInfo();
        if (network_info != null) {
            status = true;
        }
        return status;
    }

}