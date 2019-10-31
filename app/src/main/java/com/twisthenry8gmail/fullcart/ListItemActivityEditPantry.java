package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Implementation of {@link ListItemActivityEdit} for pantry items. This class should not be used
 * manually and should use {@link #buildIntent(Context, ListItem)}
 */
public class ListItemActivityEditPantry extends ListItemActivityEdit<ListItemPantry> implements ByDatePicker.Callback {

    private static final String BY_DATE_TYPE = "by_date_type";
    private static final String BY_DATE = "by_date";

    private ListItemPantry.ByDateType byDateType;
    private String byDate;

    static Intent buildIntent(Context context, ListItem item) {

        Intent intent = new Intent(context, ListItemActivityEditPantry.class);
        intent.putExtra(OLD_ITEM, item);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            byDateType = oldItem.getByDateType();
            byDate = oldItem.getByDate();
        }
        else {
            byDateType = ListItemPantry.ByDateType.values()[savedInstanceState.getInt(BY_DATE_TYPE)];
            byDate = savedInstanceState.getString(BY_DATE);
        }

        byDateTitle.setVisibility(View.VISIBLE);
        byDateView.setVisibility(View.VISIBLE);

        byDateTitle.setText(byDateType == ListItemPantry.ByDateType.BEST_BEFORE ? R.string.item_best_before_date_title : R.string.item_use_by_date_title);

        byDateTitle.setOnClickListener(v -> {

            if (byDateType == ListItemPantry.ByDateType.BEST_BEFORE) {

                byDateTitle.setText(R.string.item_use_by_date_title);
                byDateType = ListItemPantry.ByDateType.USE_BY;
            }
            else {

                byDateTitle.setText(R.string.item_best_before_date_title);
                byDateType = ListItemPantry.ByDateType.BEST_BEFORE;
            }
        });

        byDateView.setOnClickListener(v -> {

            ByDatePicker picker = ByDatePicker.getInstance(byDate, byDateType);
            picker.show(getSupportFragmentManager(), ByDatePicker.TAG);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(BY_DATE_TYPE, byDateType.ordinal());
        outState.putString(BY_DATE, byDate);
    }

    @Override
    void fillFields(String name, Category category, String notes, int quantity, boolean animate) {

        super.fillFields(name, category, notes, quantity, animate);
        fillByDate(oldItem.getByDate());
    }

    @Override
    ListItemPantry getNewItem() {

        return new ListItemPantry(getName(), getCategory(), getNotes(), getQuantity(), DateUtil.formatCurrentDate(), byDateType, byDate);
    }

    @Override
    public void onDateSet(String date) {

        this.byDate = date;
        fillByDate(date);
    }

    /**
     * Set the by date button text
     * @param date the new date
     */
    private void fillByDate(String date) {

        byDateView.setText(date.isEmpty() ? getString(R.string.item_no_by_date) : DateUtil.displayPantryDate(this, date));
    }
}
