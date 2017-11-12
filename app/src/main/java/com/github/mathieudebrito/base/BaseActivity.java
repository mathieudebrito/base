package com.github.mathieudebrito.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.mathieudebrito.base.bus.AppBus;
import com.github.mathieudebrito.base.injects.GraphRetriever;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    @Override
    public void startActivity(Intent intent) {
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
