package com.github.mathieudebrito.base.bus;

/**
 * The AppBus is the pipe for the events
 */
public interface AppBus {

    public void register(Object o);

    public void unregister(Object o);

    public void post(final Object event);

}
