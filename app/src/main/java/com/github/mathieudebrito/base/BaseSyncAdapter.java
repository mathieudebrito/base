package com.github.mathieudebrito.base;

import android.content.AbstractThreadedSyncAdapter;
import android.content.Context;

import com.github.mathieudebrito.base.injects.GraphRetriever;

/**
 * @Injectable SyncAdapter
 */
public abstract class BaseSyncAdapter extends AbstractThreadedSyncAdapter {

    public BaseSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        GraphRetriever.from(context).inject(this);
    }

    public BaseSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        GraphRetriever.from(context).inject(this);
    }
}
