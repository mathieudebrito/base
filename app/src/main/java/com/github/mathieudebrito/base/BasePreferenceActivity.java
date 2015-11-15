package com.github.mathieudebrito.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;
import com.github.mathieudebrito.utils.Logs;

import javax.inject.Inject;

/**
 * @Injectable and Eventable prefs activity
 */
public class BasePreferenceActivity extends PreferenceActivity {

    @Inject
    protected AppBus bus;

    protected Context context;
    protected Activity contextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphRetriever.from(this).inject(this);
        this.context = this;
        this.contextActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logs.debug("[BASE_PREFERENCE_ACTIVITY] onStart()");
        bus.register(this);
    }

    @Override
    protected void onStop() {
        Logs.debug("[BASE_PREFERENCE_ACTIVITY] onStop()");
        bus.unregister(this);
        super.onStop();
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent == null) {
            Logs.error("[BASE_PREFERENCE_ACTIVITY] startActivity(null)");
        }
        super.startActivity(intent);
        if (context != null) {
            this.overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);
    }

    @Override
    protected void onDestroy() {
        this.overridePendingTransition(R.anim.base_fade_in, R.anim.base_fade_out);
        super.onDestroy();
    }
}
