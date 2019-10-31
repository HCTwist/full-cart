package com.twisthenry8gmail.fullcart;

/**
 * A template for a row in a list which possesses a type as defined by {@link Type}. If you try to call
 * methods that are not compatible with the model type then an exception will be thrown
 * @param <I> the implementation of {@link ListItem} in the list
 */
abstract class Model<I extends ListItem> implements Comparable<Model<I>> {

    enum Type {

        CONTENT, STANDARD_HEADER, BASKET_HEADER
    }

    private Type type;

    // For Type.CONTENT
    private I item;

    // For Type.STANDARD_HEADER
    private Category category;

    /**
     * Checks whether an item of type {@link Type#CONTENT} belongs under this header. If this is not
     * a header, an exception will be thrown
     * @param item the item to check
     * @return true if the item belongs under the header
     */
    boolean isItemsHeader(I item) {
        if (getType() != Type.STANDARD_HEADER) {
            throw new RuntimeException("Tried to get a header object when only an item is available");
        }
        return getCategory().equals(item.getCategory());
    }

    void setType(Type type) {

        this.type = type;
    }

    Type getType() {

        return type;
    }

    void setItem(I item) {

        this.item = item;
    }

    void setCategory(Category category) {

        this.category = category;
    }

    I getItem() {
        if (getType() != Type.CONTENT) {
            throw new RuntimeException("Tried to get an item from a " + type.name() + " model");
        }
        return item;
    }

    Category getCategory() {
        if (getType() != Type.STANDARD_HEADER) {
            throw new RuntimeException("Tried to get a header category from a " + type.name() + " model");
        }
        return category;
    }
}
