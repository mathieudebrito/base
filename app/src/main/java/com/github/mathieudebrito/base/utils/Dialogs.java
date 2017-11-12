package com.github.mathieudebrito.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.mathieudebrito.base.R;
import com.github.mathieudebrito.base.spinnerwheel.WheelVerticalView;
import com.github.mathieudebrito.base.spinnerwheel.adapters.NumericWheelAdapter;

public class Dialogs {

    public interface DialogInputListener {
        public void onInputValidated(String s);
    }

    public interface DialogNumberPickerListener {
        public void onNumberPicked(int number);
    }

    public interface DialogSelectListener {
        public void onItemSelected(int position);
    }

    public interface DialogRateListener {
        public void onRateChanged(int rating);
    }

    public static Dialog notify(Context context, String message) {
        return notify(context, "!", message);
    }

    public static Dialog notify(Context context, String title, String message) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .theme(Theme.LIGHT)
                .content(message)
                .positiveText(R.string.utils_ok)
                .show();

        return dialog;
    }

    public static Dialog ask(Context context, String message, final View.OnClickListener onValidateListener) {
        return ask(context, "?", message, onValidateListener);
    }

    public static Dialog ask(final Context context, String title, String message, final View.OnClickListener onValidateListener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .theme(Theme.LIGHT)
                .positiveText(R.string.utils_validate)
                .negativeText(R.string.utils_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        onValidateListener.onClick(null);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();

        return dialog;
    }

    public static Dialog select(final Context context, String title, final String[] options, int selected, final DialogSelectListener listener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(options)
                .theme(Theme.LIGHT)
                .itemsCallbackSingleChoice(selected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int position, CharSequence charSequence) {
                        listener.onItemSelected(position);
                        return true;
                    }
                })
                .positiveText(R.string.utils_validate)
                .negativeText(R.string.utils_cancel)
                .show();

        return dialog;
    }

    public static Dialog loader(Context context) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.utils_dlg_loader, true)
                .theme(Theme.LIGHT)
                .negativeText(R.string.utils_cancel)
                .build();

        return dialog;
    }


    public static Dialog input(Context context, String title, String defaultValue, String positiveText, final DialogInputListener listener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .theme(Theme.LIGHT)
                .customView(R.layout.utils_dlg_edittext, true)
                .positiveText(positiveText)
                .negativeText(R.string.utils_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText editInput = (EditText) dialog.getCustomView().findViewById(R.id.edtInput);
                        listener.onInputValidated(editInput.getText().toString());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        EditText editInput = (EditText) dialog.getCustomView().findViewById(R.id.edtInput);
        editInput.setText(defaultValue);
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.show();
        positiveAction.setEnabled(false); // disabled by default

        return dialog;
    }

    public static Dialog numberPicker(Context context, String title, int defaultValue, final DialogNumberPickerListener listener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .customView(R.layout.utils_dlg_numberpicker, true)
                .theme(Theme.LIGHT)
                .positiveText(R.string.utils_validate)
                .negativeText(R.string.utils_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        WheelVerticalView whlNumberPicker = (WheelVerticalView) dialog.getCustomView().findViewById(R.id.whlNumberPicker);
                        listener.onNumberPicked(whlNumberPicker.getCurrentItem());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();
        WheelVerticalView whlNumberPicker = (WheelVerticalView) dialog.getCustomView().findViewById(R.id.whlNumberPicker);

        whlNumberPicker.setViewAdapter(new NumericWheelAdapter(context, 0, 100));
        whlNumberPicker.setCurrentItem(defaultValue);
        whlNumberPicker.setCyclic(true);
        whlNumberPicker.setInterpolator(new AnticipateOvershootInterpolator());

        dialog.show();

        return dialog;
    }


    public static void askForExit(final Activity context) {

        ask(context, context.getString(R.string.utils_exit), context.getString(R.string.utils_exit_app_message), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }

    public static void notifyInternetNeeded(Context context) {
        notify(context, context.getResources().getString(R.string.utils_error_internet_not_available), context.getResources().getString(R.string.utils_error_internet_not_available_feature));
    }

}
