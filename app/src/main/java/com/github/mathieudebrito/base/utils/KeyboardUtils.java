package com.github.mathieudebrito.base.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class KeyboardUtils {

    /**
     * Hide the keyboard
     * (with intelligence)
     */
    public static void hide(Activity activity) {
        try {
            if (activity != null && activity.getCurrentFocus() != null) {

                boolean edtHasFocus = activity.getCurrentFocus() instanceof EditText;
                boolean atcHasFocus = activity.getCurrentFocus() instanceof AutoCompleteTextView;

                //if (edtHasFocus || atcHasFocus) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the keyboard
     * (without intelligence)
     */
    public static void forceHide(Activity activity) {
        try {
            //if (activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Request the focus for an edit text
     * and display the keyboard
     */
    public static void requestFocus(Context context, EditText edtSearch) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        edtSearch.requestFocus();
        mgr.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
    }
}
