package com.twisthenry8gmail.fullcart;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Async task to load the pantry items
 */
class AsyncLoaderPantry extends AsyncLoader<ModelAdapterPantry, ModelPantry, ListItemPantry> {

    AsyncLoaderPantry(ModelAdapterPantry adapter) {

        super(adapter);
    }

    @Override
    protected ArrayList<ModelPantry> doInBackground(Void... voids) {

        ArrayList<Category> categories = DatabaseHelper.getInstance(getAdapter().getContext()).getCategories();

        Cursor cursor = DatabaseHelper.getInstance(getAdapter().getContext()).getReadableDatabase().query(DatabaseHelper.PANTRY_TABLE_NAME,
                null, null, null, null, null, null);

        ArrayList<ModelPantry> entries = new ArrayList<>(cursor.getCount());
        ArrayList<Category> headerCategories = new ArrayList<>();

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NAME));
            Category category = DatabaseHelper.getCategory(getAdapter().getContext(), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_CATEGORY)), categories);
            String notes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NOTES));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LIST_QUANTITY));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_DATE_ADDED));
            String packagedByDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PANTRY_BY_DATE));
            ListItemPantry.ByDateType byDateType = ListItemPantry.ByDateType.values()[Character.getNumericValue(packagedByDate.charAt(0))];
            String byDate = packagedByDate.substring(1);

            ListItemPantry item = new ListItemPantry(name, category, notes, quantity, date, byDateType, byDate);

            entries.add(ModelPantry.buildContentModel(item));

            if (!headerCategories.contains(category)) {
                headerCategories.add(category);
                entries.add(ModelPantry.buildHeaderModel(category));
            }
        }

        Collections.sort(entries);

        cursor.close();

        return entries;
    }
}
