package com.github.mathieudebrito.base.backstack.events;

public class ClearFragmentsEvent {

    private int until = 0;

    public ClearFragmentsEvent() {
    }

    public static ClearFragmentsEvent untilRoot() {
        ClearFragmentsEvent event = new ClearFragmentsEvent();
        event.until = 1;

        return event;
    }

    public int getUntil() {
        return until;
    }
}
