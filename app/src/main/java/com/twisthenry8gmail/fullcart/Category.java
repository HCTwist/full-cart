package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

/**
 * Defines a category of a pantry/list item with a color and a name
 */
class Category implements Parcelable, Comparable<Category> {

    static final String DEFAULT_NAME = "default_category_name";
    static final String DEFAULT_COLOR = "default_category_color";

    private final String name;
    private final int color;

    Category(String name, int color) {

        this.name = name;
        this.color = color;
    }

    Category(Parcel in) {

        name = in.readString();
        color = in.readInt();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeInt(color);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Category)) {
            return false;
        }

        Category category = (Category) obj;

        return category.getName().equals(getName()) && category.getColor() == getColor();
    }

    @Override
    public int hashCode() {

        return getName().hashCode();
    }

    @Override
    public int compareTo(@NonNull Category c) {

        return name.compareTo(c.name);
    }

    /**
     * @return the name of the category
     */
    String getName() {

        return name;
    }

    /**
     * @return the color of the category
     */
    int getColor() {

        return color;
    }

    /**
     * @return the default category
     */
    static Category getNoCategory(Context context) {

        return new Category(getNoCategoryName(context), getNoCategoryColor(context));
    }

    /**
     * @return the name of the default category
     */
    static String getNoCategoryName(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(DEFAULT_NAME, context.getString(R.string.default_category_name));
    }

    /**
     * @return the color of the default category
     */
    private static int getNoCategoryColor(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getInt(DEFAULT_COLOR, ContextCompat.getColor(context, R.color.default_category));
    }

    /**
     * Adds the name and color to a content values set
     *
     * @return the content values associated with the category
     */
    ContentValues toContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CATEGORIES_NAME, getName());
        contentValues.put(DatabaseHelper.CATEGORIES_COLOR, getColor());
        return contentValues;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {

            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {

            return new Category[size];
        }
    };
}
