package com.twisthenry8gmail.fullcart;

import java.util.ArrayList;

interface ListFragmentInterface<I extends ListItem> {

    /**
     * Adds items to the database and adapter using an {@link ItemDelegator}
     *
     * @param items       the items to add
     * @param addToSearch if true, the items will be added to the search database
     */
    void addItems(ArrayList<I> items, boolean addToSearch);

    /**
     * Removes an item from the database and reflects the change in the adapter
     *
     * @param item the item to remove
     */
    void removeItem(I item);

    /**
     * Edit an item in the database and reflect the change in the adapter using an {@link ItemDelegator}
     *
     * @param oldItem the item to edit
     * @param newItem the new item
     */
    void editItem(I oldItem, I newItem);
}
