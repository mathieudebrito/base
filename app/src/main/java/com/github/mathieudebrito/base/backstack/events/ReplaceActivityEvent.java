package com.github.mathieudebrito.base.backstack.events;

import android.content.Intent;

public class ReplaceActivityEvent {

    public Intent intent;

    public ReplaceActivityEvent(Intent intent) {
        this.intent = intent;
    }

}
