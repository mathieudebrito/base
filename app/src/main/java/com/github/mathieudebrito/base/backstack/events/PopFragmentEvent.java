package com.github.mathieudebrito.base.backstack.events;

public class PopFragmentEvent {

    public boolean shouldResume = true;

    /**
     * Until which fragment to resume
     */
    public String until = null;

    public PopFragmentEvent() {
    }

    public PopFragmentEvent(Class until) {
        this.until = until.getName();
    }

    public PopFragmentEvent(boolean shouldResume) {
        this.shouldResume = shouldResume;
    }
}
