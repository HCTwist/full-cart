package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;

import androidx.annotation.NonNull;

import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_CATEGORY;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_CHECKED;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_DATE_ADDED;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_NAME;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_NOTES;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_QUANTITY;

/**
 * An implementation of {@link ListItem} with an additional checked property
 */
public class ListItemShopping extends ListItem implements Comparable<ListItemShopping> {

    private boolean checked;

    ListItemShopping(String name, Category category, String notes, int quantity, String date, boolean checked) {

        super(name, category, notes, quantity, date);
        this.checked = checked;
    }

    ListItemShopping(ListItemShopping item) {

        super(item.getName(), item.getCategory(), item.getNotes(), item.getQuantity(), item.getDate());
        checked = item.isChecked();
    }

    private ListItemShopping(Parcel in) {

        super(in);
        checked = (boolean) in.readValue(boolean.class.getClassLoader());
    }

    @Override
    public boolean isSimilarTo(ListItem item) {

        if (!(item instanceof ListItemShopping)) {
            return false;
        }

        ListItemShopping i = (ListItemShopping) item;

        return i.getName().equals(getName()) && i.getCategory().equals(getCategory())
                && i.getNotes().equals(getNotes()) && i.isChecked() == isChecked();
    }

    @Override
    public String getWhereClause() {

        return LIST_NAME + "=? AND " +
                LIST_CATEGORY + "=? AND " +
                LIST_NOTES + "=? AND " +
                LIST_QUANTITY + "=? AND " +
                LIST_DATE_ADDED + "=? AND " +
                LIST_CHECKED + "=?";
    }

    @Override
    public String[] getWhereArgs() {

        return new String[]{getName(), getCategory().getName(), getNotes(), getQuantity() + "", getDate(), (isChecked() ? 1 : 0) + ""};
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ListItemShopping)) {
            return false;
        }

        ListItemShopping i = (ListItemShopping) obj;

        return i.getName().equals(getName()) && i.getCategory().equals(getCategory())
                && i.getNotes().equals(getNotes()) && i.getQuantity() == getQuantity()
                && i.getDate().equals(getDate()) && i.isChecked() == isChecked();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeValue(checked);
    }

    @Override
    public int compareTo(@NonNull ListItemShopping item) {

        if (item.isChecked() == isChecked()) {

            int dateComp = getDate().compareTo(item.getDate());
            return dateComp == 0 ? getName().compareTo(item.getName()) : dateComp;
        }
        else {

            return isChecked() ? -1 : 1;
        }
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues contentValues = super.toContentValues();
        contentValues.put(LIST_CHECKED, isChecked());
        return contentValues;
    }

    /**
     * @return a string representing the extra content of this item to be displayed in a list
     */
    String getMainString() {

        return getQuantity() == 1 ? getName() : getName() + " - " + getQuantity();
    }

    /**
     * @see ListItemPantry#getByDateTypeString(Context)
     */
    String getExtraString() {

        return getNotes();
    }

    boolean isChecked() {

        return checked;
    }

    void setChecked(boolean checked) {

        this.checked = checked;
    }

    public static final Creator<ListItemShopping> CREATOR = new Creator<ListItemShopping>() {
        @Override
        public ListItemShopping createFromParcel(Parcel in) {

            return new ListItemShopping(in);
        }

        @Override
        public ListItemShopping[] newArray(int size) {

            return new ListItemShopping[size];
        }
    };
}
