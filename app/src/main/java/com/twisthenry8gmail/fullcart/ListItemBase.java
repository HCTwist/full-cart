package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple class for items defining a name and a category
 */
abstract class ListItemBase implements Parcelable {

    private String name;
    private Category category;

    ListItemBase(String name, Category category) {

        this.name = name;
        this.category = category;
    }

    ListItemBase(Parcel in) {

        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeParcelable(category, flags);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    /**
     * @return the item's properties as a {@link ContentValues} object to be used with SQLite
     */
    ContentValues toContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LIST_NAME, getName());
        contentValues.put(DatabaseHelper.LIST_CATEGORY, getCategory().getName());

        return contentValues;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Category getCategory() {

        return category;
    }

    public void setCategory(Category category) {

        this.category = category;
    }
}
