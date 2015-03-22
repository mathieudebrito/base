package com.github.mathieudebrito.base;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.github.mathieudebrito.base.injects.GraphRetriever;

/**
 * @Injectable ListFragment
 */
public class BaseListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphRetriever.from(getActivity()).inject(this);
    }
}
