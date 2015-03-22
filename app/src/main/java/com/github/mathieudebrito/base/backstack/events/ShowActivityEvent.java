package com.github.mathieudebrito.base.backstack.events;

import android.content.Intent;

public class ShowActivityEvent {

    public final Intent intent;

    public ShowActivityEvent(Intent intent) {
        this.intent = intent;
    }
}
