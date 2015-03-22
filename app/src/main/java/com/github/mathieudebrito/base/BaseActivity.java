package com.github.mathieudebrito.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.crashlytics.android.Crashlytics;
import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;
import com.github.mathieudebrito.utils.Logs;

import javax.inject.Inject;


/**
 * @Injectable and Eventable activity
 */
public class BaseActivity extends ActionBarActivity {

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

        // Just in case (already added in App)
        Crashlytics.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logs.debug(this, "onStart()");
        bus.register(this);
    }

    @Override
    protected void onStop() {
        Logs.debug(this, "onStop()");
        bus.unregister(this);
        super.onStop();
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent == null) {
            Logs.error(this, "startActivity(null)");
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