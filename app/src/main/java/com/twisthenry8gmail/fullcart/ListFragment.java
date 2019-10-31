package com.twisthenry8gmail.fullcart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Fragment that handles displaying a list injected by a {@link ModelAdapter}
 *
 * @param <M> the implementation of {@link Model}
 * @param <I> the implementation of {@link ListItem}
 */
abstract class ListFragment<M extends Model<I>, I extends ListItem> extends Fragment implements DeleteDialogFragment.Callback, MainActivity.FABClick, MainActivity.AdapterLoader, ListFragmentInterface<I> {

    private ItemDelegator<M, I> itemDelegator;

    private ModelAdapter<M, I> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(getLayoutResource(), container, false);

        RecyclerView recyclerView = layout.findViewById(getListResource());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = createAdapter();
        adapter.setLoadingView(layout.findViewById(getLoadingViewResource()));
        adapter.setEmptyView(layout.findViewById(getEmptyViewResource()));
        recyclerView.setAdapter(adapter);

        itemDelegator = new ItemDelegator<M, I>(adapter) {
            @Override
            protected void updateDatabase(I oldItem, I newItem) {

                ListFragment.this.updateDatabase(oldItem, newItem);
            }

            @Override
            protected void addToDatabase(I item) {

                ListFragment.this.addToDatabase(item);
            }

            @Override
            protected void removeFromDatabase(I item) {

                ListFragment.this.deleteFromDatabase(item);
            }
        };

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {

            Bundle extras = data.getExtras();
            if (requestCode == RequestCodes.REQUEST_SETTINGS) {

                ArrayList<String> keysChanged = extras.getStringArrayList(SettingsFragment.SETTINGS_DATA);
                onSettingChanged(keysChanged);
            }
            else if (requestCode == RequestCodes.REQUEST_ITEM_ADD) {

                ArrayList<I> items = data.getParcelableArrayListExtra(ListItemActivityAdd.ITEMS);
                addItems(items, true);
            }
            else if (requestCode == RequestCodes.REQUEST_ITEM_EDIT) {

                I item = data.getParcelableExtra(ListItemActivityEdit.OLD_ITEM);
                if (data.getBooleanExtra(ListItemActivityEdit.DELETE, false)) {

                    removeItem(item);
                }
                else {

                    I newItem = data.getParcelableExtra(ListItemActivityEdit.NEW_ITEM);
                    editItem(item, newItem);
                }
            }
            else if (requestCode == RequestCodes.REQUEST_CATEGORIES) {

                ArrayList<Category> changeKeys = data.getParcelableArrayListExtra(CategoriesActivity.CHANGE_KEYS);
                ArrayList<Category> changeValues = data.getParcelableArrayListExtra(CategoriesActivity.CHANGE_VALUES);

                adapter.post(() -> adapter.notifyCategoriesChanged(changeKeys, changeValues));
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            if (requestCode == RequestCodes.REQUEST_ITEM_ADD
                    || requestCode == RequestCodes.REQUEST_ITEM_EDIT) {

                if (getView() != null) {
                    AnchoredSnackbar.make(getView(), R.string.discarded, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Called when a header is long pressed and ready to delete
    @Override
    public void delete(int position) {

        ArrayList<M> entries = getAdapter().getEntries();
        ArrayList<I> removeQueue = new ArrayList<>();

        for (int i = position + 1; i < entries.size(); i++) {

            M model = entries.get(i);
            if (model.getType() == Model.Type.CONTENT) {
                removeQueue.add(model.getItem());
            }
            else {
                break;
            }
        }

        for (I item : removeQueue) {
            removeItem(item);
        }
    }

    @Override
    public void loadItems(Runnable callback) {

        adapter.attemptLoad();
        adapter.post(callback);
    }

    @Override
    public void addItems(ArrayList<I> items, boolean addToSearch) {

        adapter.post(() -> {
            ItemDelegator.Add addDelegator = itemDelegator.new Add(items);

            addDelegator.commitToAdapter();
            addDelegator.commitToDatabase();
            if (addToSearch) addDelegator.commitToSearch();
        });
    }

    @Override
    public void editItem(I oldItem, I newItem) {

        adapter.post(() -> {
            ItemDelegator.Edit editDelegator = itemDelegator.new Edit(oldItem, newItem);
            editDelegator.commitEdit();
        });
    }

    @Override
    public void removeItem(I item) {

        adapter.removeItem(item);
        deleteFromDatabase(item);
    }

    /**
     * @return the {@link ModelAdapter} being used
     */
    ModelAdapter<M, I> getAdapter() {

        return adapter;
    }

    /**
     * @return a list of all categories being used by the adapter currently
     */
    Set<Category> getUsedCategories() {

        Set<Category> categories = new HashSet<>();

        for (M model : adapter.getEntries()) {
            if (model.getType() == Model.Type.STANDARD_HEADER) {
                categories.add(model.getCategory());
            }
        }

        return categories;
    }

    /**
     * @return the main layout resource
     */
    @LayoutRes
    abstract int getLayoutResource();

    /**
     * @return the ID of a {@link RecyclerView} to display the list in
     */
    @IdRes
    abstract int getListResource();

    /**
     * @return create the relevant {@link ModelAdapter} to inject the list
     */
    abstract ModelAdapter<M, I> createAdapter();

    /**
     * @return the ID of the view to display when the data is being loaded
     */
    @IdRes
    abstract int getLoadingViewResource();

    /**
     * @return the ID of the view to display if the date set is empty
     */
    @IdRes
    abstract int getEmptyViewResource();

    /**
     * Should update the relevant database to reflect editing an item
     *
     * @param oldItem the existing item to edit
     * @param newItem the new item
     */
    abstract void updateDatabase(I oldItem, I newItem);

    /**
     * Should add an item to the database
     *
     * @param item the item to add
     */
    abstract void addToDatabase(I item);

    /**
     * Should delete an item from the database
     *
     * @param item the item to delete
     */
    abstract void deleteFromDatabase(I item);

    /**
     * Provides a list of keys changed from settings
     *
     * @param keys a list of keys
     */
    abstract void onSettingChanged(ArrayList<String> keys);

    /**
     * Should show a {@link com.twisthenry8gmail.roundedbottomsheet.BottomRoundedFragment} with actions relevant to the list
     */
    abstract void showOverflowMenu();

    /**
     * Called when an item is clicked from the overflow menu
     *
     * @param position the position of the item clicked
     * @see #showOverflowMenu()
     */
    abstract void menuClick(int position);
}
