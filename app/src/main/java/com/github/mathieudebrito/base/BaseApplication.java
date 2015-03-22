package com.github.mathieudebrito.base;

import android.app.Application;

import com.github.mathieudebrito.base.injects.GraphRetriever;

import dagger.ObjectGraph;

/**
 * Creates the graph that will allow injection
 */
public class BaseApplication extends Application implements GraphRetriever.GraphApplication {

    protected ObjectGraph graph;

    protected void inject(Object module) {
        graph = ObjectGraph.create(module);
        graph.inject(this);
    }

    @Override
    public ObjectGraph getGraph() {
        return graph;
    }
}
