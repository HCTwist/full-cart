package com.twisthenry8gmail.fullcart;

import android.content.ContentValues;
import android.os.Parcel;

import androidx.annotation.NonNull;

public class ListItemSearch extends ListItemBase implements Comparable<ListItemSearch> {

    private static final double DECAY = 0.9;

    private final double frequency;
    private final String lastAdded;

    ListItemSearch(String name, Category category, double frequency, String lastAdded) {

        super(name, category);
        this.frequency = frequency;
        this.lastAdded = lastAdded;
    }

    private ListItemSearch(Parcel in) {

        super(in);
        frequency = in.readDouble();
        lastAdded = in.readString();
    }

    private double getFrequency() {

        return frequency;
    }

    private String getLastAdded() {

        return lastAdded;
    }

    private double getScore() {

        return getFrequency() * Math.pow(DECAY, DateUtil.getCurrentDateDifference(getLastAdded()));
    }

    /**
     * @see ListItemBase#toContentValues()
     */
    @Override
    ContentValues toContentValues() {

        ContentValues contentValues = super.toContentValues();
        contentValues.put(DatabaseHelper.SEARCH_FREQUENCY, getFrequency());
        contentValues.put(DatabaseHelper.SEARCH_LAST_ADDED, getLastAdded());

        return contentValues;
    }

    @Override
    public int compareTo(@NonNull ListItemSearch item) {

        return (int) Math.round(item.getScore() - getScore());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeDouble(frequency);
        dest.writeString(lastAdded);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    public static final Creator<ListItemSearch> CREATOR = new Creator<ListItemSearch>() {
        @Override
        public ListItemSearch createFromParcel(Parcel in) {

            return new ListItemSearch(in);
        }

        @Override
        public ListItemSearch[] newArray(int size) {

            return new ListItemSearch[size];
        }
    };
}
