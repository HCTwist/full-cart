package com.twisthenry8gmail.fullcart;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Async task to load the shopping items
 */
class AsyncLoaderShopping extends AsyncLoader<ModelAdapterShopping, ModelShopping, ListItemShopping> {

    AsyncLoaderShopping(ModelAdapterShopping adapter) {

        super(adapter);
    }

    @Override
    protected ArrayList<ModelShopping> doInBackground(Void... voids) {

        ArrayList<Category> categories = DatabaseHelper.getInstance(getAdapter().getContext()).getCategories();

        Cursor cursor = DatabaseHelper.getInstance(getAdapter().getContext()).getReadableDatabase().query(DatabaseHelper.LIST_TABLE_NAME,
                null, null, null, null, null, DatabaseHelper.LIST_CATEGORY);

        ArrayList<ModelShopping> entries = new ArrayList<>(cursor.getCount());
        ArrayList<Category> headerCategories = new ArrayList<>();
        ArrayList<Category> checkedHeaderCategories = new ArrayList<>();

        int autoDelete = SettingsFragment.getCheckedAutoDelete(getAdapter().getContext());

        boolean basketHeaderAdded = false;

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NAME));
            Category category = DatabaseHelper.getCategory(getAdapter().getContext(), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_CATEGORY)), categories);
            String notes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_NOTES));
            int number = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LIST_QUANTITY));
            boolean checked = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LIST_CHECKED)) == 1;
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LIST_DATE_ADDED));

            ListItemShopping item = new ListItemShopping(name, category, notes, number, date, checked);

            //Delete if necessary
            if (autoDelete != 0 && checked && -DateUtil.getCurrentDateDifference(date) >= autoDelete) {

                DatabaseHelper.getInstance(getAdapter().getContext()).deleteShoppingItem(item);
            }
            else {

                entries.add(ModelShopping.buildContentModel(item));
                if (checked && !checkedHeaderCategories.contains(category)) {

                    entries.add(ModelShopping.buildHeaderModel(category, true));
                    checkedHeaderCategories.add(category);
                }
                else if (!checked && !headerCategories.contains(category)) {

                    entries.add(ModelShopping.buildHeaderModel(category, false));
                    headerCategories.add(category);
                }

                if (!basketHeaderAdded && checked) {

                    entries.add(ModelShopping.buildBasketHeaderModel());
                    basketHeaderAdded = true;
                }
            }
        }

        Collections.sort(entries);

        cursor.close();
        return entries;
    }
}
