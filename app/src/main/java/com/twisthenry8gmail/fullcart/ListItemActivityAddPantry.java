package com.twisthenry8gmail.fullcart;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Implementation of {@link ListItemActivityAdd} for pantry items
 */
public class ListItemActivityAddPantry extends ListItemActivityAdd<ListItemPantry> implements ByDatePicker.Callback {

    private static final String QUICK_ADD_ITEM = "quick_add_item";

    private ListItemSearch quickAddItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            quickAddItem = savedInstanceState.getParcelable(QUICK_ADD_ITEM);
        }

        byDateTitle.setVisibility(View.VISIBLE);
        byDateView.setVisibility(View.VISIBLE);

        registerByDateType(SettingsFragment.getDefaultByDateType(this));
        byDateTitle.setOnClickListener(v -> registerByDateType(items.get(current).getByDateType()));

        byDateView.setOnClickListener(v -> {

            ByDatePicker datePicker = ByDatePicker.getInstance(items.get(current).getByDate(), SettingsFragment.getDefaultByDateType(this));
            datePicker.show(getSupportFragmentManager(), ByDatePicker.TAG);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (quickAddItem != null) {
            outState.putParcelable(QUICK_ADD_ITEM, quickAddItem);
        }
    }

    @Override
    void fillFields(String name, Category category, String notes, int quantity, boolean animate) {

        super.fillFields(name, category, notes, quantity, animate);
        fillByDate(items.get(current).getByDate());
    }

    @Override
    public void onSearchItemQuickAddClicked(ListItemSearch item) {

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.KEY_ASK_USE_BY, true)) {

            quickAddItem = item;

            ByDatePicker picker = ByDatePicker.getInstance(DateUtil.formatCurrentDate(), SettingsFragment.getDefaultByDateType(this));
            picker.show(getSupportFragmentManager(), ByDatePicker.TAG);
        }
        else {

            super.onSearchItemQuickAddClicked(item);
        }
    }

    @Override
    void addNewItem(String name, Category category) {

        ListItemPantry.ByDateType dateType = SettingsFragment.getDefaultByDateType(this);
        items.add(new ListItemPantry(name, category, "", 1, "", dateType, ""));
    }

    @Override
    public void onDateSet(String date) {

        if (quickAddItem != null) {

            items.add(new ListItemPantry(quickAddItem.getName(), quickAddItem.getCategory(), "",
                    1, "", SettingsFragment.getDefaultByDateType(this), date));
            sendResult();
        }
        else {

            fillByDate(date);
            items.get(current).setByDate(date);
        }
    }

    /**
     * Change the date type of the current item. This also changes the by date button text
     * @param dateType the new date type
     */
    private void registerByDateType(ListItemPantry.ByDateType dateType) {

        byDateTitle.setText(dateType == ListItemPantry.ByDateType.BEST_BEFORE ?
                R.string.item_best_before_date_title : R.string.item_use_by_date_title);
        if (!items.isEmpty()) items.get(current).setByDateType(dateType);
    }

    /**
     * Set the by date button text
     * @param date the new date
     */
    private void fillByDate(String date) {

        byDateView.setText(date.isEmpty() ? getString(R.string.item_no_by_date) : DateUtil.displayPantryDate(this, date));
    }
}
