package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.preference.PreferenceManager;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;

import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_CATEGORY;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_DATE_ADDED;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_NAME;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_NOTES;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.LIST_QUANTITY;
import static com.twisthenry8gmail.fullcart.DatabaseHelper.PANTRY_BY_DATE;

/**
 * A list item with a use by/best before date
 */
public class ListItemPantry extends ListItem implements Comparable<ListItemPantry> {

    enum ByDateType {

        USE_BY(R.array.use_by_date_displays), BEST_BEFORE(R.array.best_before_date_displays);

        @ArrayRes
        final
        int displayRes;

        ByDateType(@ArrayRes int displayRes) {

            this.displayRes = displayRes;
        }

        String[] getDisplay(Context context) {

            return context.getResources().getStringArray(displayRes);
        }
    }

    private ByDateType byDateType;
    private String byDate;

    public ListItemPantry(String name, Category category, String notes, int quantity, String date, ByDateType byDateType, String byDate) {

        super(name, category, notes, quantity, date);
        this.byDateType = byDateType;
        this.byDate = byDate;
    }

    private ListItemPantry(Parcel in) {

        super(in);
        byDateType = ByDateType.values()[in.readInt()];
        byDate = in.readString();
    }


    @Override
    public boolean isSimilarTo(ListItem item) {

        if (!(item instanceof ListItemPantry)) {
            return false;
        }

        ListItemPantry i = (ListItemPantry) item;

        return i.getName().equals(getName()) && i.getCategory().equals(getCategory())
                && i.getNotes().equals(getNotes())
                && i.getByDateType() == getByDateType()
                && i.getByDate().equals(getByDate());
    }

    @Override
    public String getWhereClause() {

        return LIST_NAME + "=? AND " +
                LIST_CATEGORY + "=? AND " +
                LIST_NOTES + "=? AND " +
                LIST_QUANTITY + "=? AND " +
                LIST_DATE_ADDED + "=? AND " +
                PANTRY_BY_DATE + "=?";
    }

    @Override
    public String[] getWhereArgs() {

        return new String[]{getName(), getCategory().getName(), getNotes(), String.valueOf(getQuantity()), getDate(), getPackagedByDate()};
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ListItemPantry)) {
            return false;
        }

        ListItemPantry i = (ListItemPantry) obj;

        return i.getName().equals(getName()) && i.getCategory().equals(getCategory())
                && i.getNotes().equals(getNotes()) && i.getQuantity() == getQuantity()
                && i.getDate().equals(getDate()) && i.getByDateType() == getByDateType() && i.getByDate().equals(getByDate());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeInt(byDateType.ordinal());
        dest.writeString(byDate);
    }

    @Override
    public int compareTo(@NonNull ListItemPantry item) {

        int byDateComp = getByDate().compareTo(item.getByDate());

        if (byDateComp == 0 || (hasByDate() && item.hasByDate())) {
            return getName().compareTo(item.getName());
        }
        else {
            return hasByDate() ? 1 : item.hasByDate() ? -1 : byDateComp;
        }
    }

    @Override
    ContentValues toContentValues() {

        ContentValues contentValues = super.toContentValues();
        contentValues.put(PANTRY_BY_DATE, getPackagedByDate());
        return contentValues;
    }

    /**
     * @return a string representing the main content of this item to be displayed in a list
     */
    String getMainString() {

        return getName();
    }

    /**
     * @param context the context
     * @return a string representing the by date type of this item, formatted according to user preference
     * @see SettingsFragment#KEY_PANTRY_DATE_FORMAT
     */
    String getByDateTypeString(Context context) {

        if (hasByDate()) {

            int prefIndex = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(SettingsFragment.KEY_PANTRY_DATE_FORMAT, "0"));
            return getByDateType().getDisplay(context)[prefIndex];
        }

        return "";
    }

    /**
     * @return true if the item has a by date set
     */
    boolean hasByDate() {

        return !getByDate().isEmpty();
    }

    /**
     * @return the by date prefixed with the ordinal of it's date type
     * @see ByDateType
     */
    private String getPackagedByDate() {

        return getByDateType().ordinal() + getByDate();
    }

    ByDateType getByDateType() {

        return byDateType;
    }

    String getByDate() {

        return byDate;
    }

    void setByDateType(ByDateType type) {

        this.byDateType = type;
    }

    void setByDate(String byDate) {

        this.byDate = byDate;
    }

    public static final Creator<ListItemPantry> CREATOR = new Creator<ListItemPantry>() {
        @Override
        public ListItemPantry createFromParcel(Parcel in) {

            return new ListItemPantry(in);
        }

        @Override
        public ListItemPantry[] newArray(int size) {

            return new ListItemPantry[size];
        }
    };
}
