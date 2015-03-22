package com.github.mathieudebrito.base.injects;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import dagger.ObjectGraph;

/**
 * The GraphRetriever retrieves the dependency Graph from a Class
 * (once the Graph is retrieved, @Inject objects will be injected)
 *
 * This doesn't cover all the cases (broadcastReceiver, IntentService, etc)
 */
public class GraphRetriever {

    public interface GraphApplication {
        ObjectGraph getGraph();
    }

    public static ObjectGraph from(Activity activity) {
        GraphApplication application = (GraphApplication) activity.getApplication();
        return application.getGraph();
    }

    public static ObjectGraph from(Service service) {
        GraphApplication application = (GraphApplication) service.getApplication();
        return application.getGraph();
    }

    public static ObjectGraph from(Context context) {
        GraphApplication application = (GraphApplication) context.getApplicationContext();
        return application.getGraph();
    }
}