package com.twisthenry8gmail.fullcart;

/**
 * Implementation of {@link ListItemActivity} for shopping items
 */
public class ListItemActivityAddShopping extends ListItemActivityAdd<ListItemShopping> {

    @Override
    void addNewItem(String name, Category category) {

        items.add(new ListItemShopping(name, category, "", 1, "", false));
    }
}
