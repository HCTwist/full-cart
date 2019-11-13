package com.twisthenry8gmail.fullcart;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility class to manage adding and editing items to both the database and list
 *
 * @param <M> the {@link Model} implementation
 * @param <I> the {@link ListItem} implementation
 */
abstract class ItemDelegator<M extends Model<I>, I extends ListItem> {

    private final ModelAdapter<M, I> adapter;

    ItemDelegator(ModelAdapter<M, I> adapter) {

        this.adapter = adapter;
    }

    /**
     * Convenience method to get an instance of the {@link DatabaseHelper}
     *
     * @return the database helper instance
     * @see DatabaseHelper#getInstance(Context)
     */
    private DatabaseHelper getDatabase() {

        return DatabaseHelper.getInstance(adapter.getContext());
    }

    /**
     * Should update an item in the database
     *
     * @param oldItem the existing item to change
     * @param newItem the new item
     */
    protected abstract void updateDatabase(I oldItem, I newItem);

    /**
     * Should add an item to the database
     *
     * @param item the item to add
     */
    protected abstract void addToDatabase(I item);

    /**
     * Should remove an item from the database
     *
     * @param item the item to remove
     */
    protected abstract void removeFromDatabase(I item);

    /**
     * Class to manage adding multiple items to the database
     */
    class Add {

        private final ArrayList<I> addQueue;

        private final ArrayList<I> oldItems = new ArrayList<>();
        private final ArrayList<I> newItems = new ArrayList<>();

        Add(ArrayList<I> itemsToAdd) {

            addQueue = itemsToAdd;
            filter();
        }

        /**
         * Sorts the items into items to edit and items needing to be added
         */
        private void filter() {

            Iterator<I> iterator = addQueue.iterator();
            while (iterator.hasNext()) {

                I item = iterator.next();

                for (Model<I> model : adapter.getEntries()) {

                    if (model.getType() == Model.Type.CONTENT) {

                        I i = model.getItem();
                        if (model.getItem().isSimilarTo(item)) {

                            oldItems.add(i);
                            item.setQuantity(i.getQuantity() + item.getQuantity());
                            newItems.add(item);
                            iterator.remove();
                        }
                    }
                }
            }
        }

        /**
         * Update the adapter with the filtered items
         */
        void commitToAdapter() {

            if (oldItems.size() != newItems.size()) {
                throw new RuntimeException("Array sizes do not match");
            }

            if (adapter.isLoaded()) {
                for (int i = 0; i < oldItems.size(); i++) {

                    I oldItem = oldItems.get(i);
                    I newItem = newItems.get(i);

                    adapter.editItem(oldItem, newItem);
                }

                adapter.addItems(addQueue);
            }
        }

        /**
         * Update the database with the filtered items
         */
        void commitToDatabase() {

            for (int i = 0; i < oldItems.size(); i++) {

                I oldItem = oldItems.get(i);
                I newItem = newItems.get(i);

                updateDatabase(oldItem, newItem);
            }

            for (I item : addQueue) {

                addToDatabase(item);
            }
        }

        /**
         * Update the search database with the filtered items
         */
        void commitToSearch() {

            for (I item : addQueue) {

                getDatabase().addSearchItem(item.getName(), item.getCategory().getName());
            }

            for (I item : newItems) {

                getDatabase().addSearchItem(item.getName(), item.getCategory().getName());
            }
        }
    }

    /**
     * Class to manage editing a single item
     */
    class Edit {

        private final I oldItem;
        private final I newItem;

        Edit(I oldItem, I newItem) {

            this.oldItem = oldItem;
            this.newItem = newItem;
        }

        /**
         * Checks whether the item needs to be edited or combined with another, and commits the change
         * to both the database and the adapter
         */
        void commitEdit() {

            if (!oldItem.isSimilarTo(newItem)) {

                for (Model<I> model : adapter.getEntries()) {

                    if (model.getType() == Model.Type.CONTENT) {

                        I i = model.getItem();

                        if (newItem.isSimilarTo(i)) {

                            adapter.removeItem(oldItem);

                            newItem.setQuantity(i.getQuantity() + newItem.getQuantity());
                            adapter.editItem(i, newItem);

                            removeFromDatabase(oldItem);

                            updateDatabase(i, newItem);
                            return;
                        }
                    }
                }
            }

            adapter.editItem(oldItem, newItem);

            removeFromDatabase(oldItem);
            addToDatabase(newItem);
        }
    }
}
