package com.twisthenry8gmail.fullcart;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Class that controls the loading of the {@link ModelAdapter}'s
 *
 * @param <A> the adapter class
 * @param <M> the model class
 * @param <I> the item class
 */
abstract class AsyncLoader<A extends ModelAdapter<M, I>, M extends Model<I>, I extends ListItem> extends AsyncTask<Void, Void, ArrayList<M>> {

    private final A adapter;

    AsyncLoader(A adapter) {

        this.adapter = adapter;
    }

    @Override
    protected void onPostExecute(ArrayList<M> ms) {

        adapter.populate(ms);
        adapter.notifyLoadingFinished();
    }

    /**
     * @return the adapter being loaded
     */
    A getAdapter() {

        return adapter;
    }
}
