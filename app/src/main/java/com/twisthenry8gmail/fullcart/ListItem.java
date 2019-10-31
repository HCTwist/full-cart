package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An extended representation of {@link ListItemBase} adding notes, quantity and creation date
 */
abstract class ListItem extends ListItemBase implements Parcelable {

    private String notes;
    private int quantity;
    private String date;

    ListItem(String name, Category category, String notes, int quantity, String date) {

        super(name, category);
        this.notes = notes;
        this.quantity = quantity;
        this.date = date;
    }

    ListItem(Parcel in) {

        super(in);
        notes = in.readString();
        quantity = in.readInt();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeString(notes);
        dest.writeInt(quantity);
        dest.writeString(date);
    }

    @Override
    ContentValues toContentValues() {

        ContentValues contentValues = super.toContentValues();
        contentValues.put(DatabaseHelper.LIST_NOTES, getNotes());
        contentValues.put(DatabaseHelper.LIST_QUANTITY, getQuantity());
        contentValues.put(DatabaseHelper.LIST_DATE_ADDED, getDate());

        return contentValues;
    }

    String getNotes() {

        return notes;
    }

    void setNotes(String notes) {

        this.notes = notes;
    }

    int getQuantity() {

        return quantity;
    }

    void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    String getDate() {

        return date;
    }

    void setDate(String date) {

        this.date = date;
    }

    /**
     * Should check whether two items are identical ignoring their quantity
     * @param item the item to check against
     * @return true if the items are similar
     */
    public abstract boolean isSimilarTo(ListItem item);

    /**
     * @return an SQLite where clause to check equality
     */
    public abstract String getWhereClause();

    /**
     * @return SQLite where arguments to be used with the corresponding where clause
     * @see #getWhereClause()
     */
    public abstract String[] getWhereArgs();
}
