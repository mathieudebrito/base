package com.github.mathieudebrito.base.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mathieudebrito.base.R;


public class Toasts {

    public static void show(Context context, int message) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void warnInternet(Context context) {
        warn(context, R.string.utils_error_internet_not_available);
    }

    public static void ok(Context context, int message) {
        showMessageWithIcon(context, message, -1);
    }

    public static void warn(Context context, int message) {
        showMessageWithIcon(context, message, -1);
    }

    public static void error(Context context, int message) {
        showMessageWithIcon(context, message, -1);
    }

    public static void showMessageWithIcon(Context context, int message, int icon) {
        showMessageWithIcon(context, context.getString(message), icon);
    }

    public static void showMessageWithIcon(Context context, String message, int icon) {
        try {
            View layout = LayoutInflater.from(context).inflate(R.layout.toa_with_icon, null);

            // Add image
            ImageView imgIcon = (ImageView) layout.findViewById(R.id.imgIcon);
            imgIcon.setImageResource(icon);

            // Add textView
            TextView txtMessage = (TextView) layout.findViewById(R.id.txtMessage);
            txtMessage.setText(message);

            Toast toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}