package com.twisthenry8gmail.fullcart;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Recycler adapter implementation that displays two extra views, one when the list is empty and one
 * before it has been provided with any data
 *
 * @param <VH> the {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} holder implementation
 */
abstract class EmptyRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private boolean loaded = false;
    private boolean empty = false;

    private View loadingView;
    private View emptyView;

    private ArrayList<Runnable> tasks = new ArrayList<>();

    EmptyRecyclerAdapter() {

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {

                setEmpty(false);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {

                setEmpty(getItemCount() == 0);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        setEmpty(getItemCount() == 0);
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Checks whether data has been provided to the adapter
     *
     * @return true if the adapter data has been provided
     */
    boolean isLoaded() {

        return loaded;
    }

    /**
     * Defines the view to display before the adapter has any data
     *
     * @param v the view to display
     */
    void setLoadingView(View v) {

        loadingView = v;
    }

    /**
     * Defines the view to display when the data set is empty
     *
     * @param v the view to display
     */
    void setEmptyView(View v) {

        emptyView = v;
    }

    /**
     * Notify the adapter that all the data has been loaded
     */
    void notifyLoadingFinished() {

        loaded = true;
        if (loadingView != null) {
            AnimationUtil.push(loadingView, false);
        }
        setEmpty(getItemCount() == 0);

        for (Runnable task : tasks) {
            task.run();
        }
    }

    /**
     * Hide or show the empty view if there's been a change
     *
     * @param empty if set to true, the empty view will be shown
     */
    private void setEmpty(final boolean empty) {

        if (!(loadingView != null && !loaded) && emptyView != null && this.empty != empty) {

            this.empty = empty;
            AnimationUtil.push(emptyView, empty);
        }
    }

    /**
     * Attempt to load the data set. This can be called safely multiple times and will only execute once.
     * This relies on implementation being provided via the {@link #loadItems()} method
     */
    void attemptLoad() {

        if (!loaded) {
            loadItems();
            loaded = true;
        }
    }

    /**
     * Schedule a task to be run once the adapter has completed loading. If the adapter has already
     * loaded, the task is run immediately
     *
     * @param task the task to execute
     */
    void post(Runnable task) {

        if (task != null) {
            if (loaded) {
                task.run();
            }
            else {
                tasks.add(task);
            }
        }
    }

    /**
     * Optional method to override to facilitate loading
     */
    void loadItems() {

    }
}
