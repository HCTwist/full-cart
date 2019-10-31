package com.twisthenry8gmail.fullcart;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Implementation of {@link ListItemActivity} for editing items
 *
 * @param <I>
 */
abstract class ListItemActivityEdit<I extends ListItem> extends ListItemActivity {

    static final String SPINNER_POSITION = "spinner_position";
    static final String OLD_ITEM = "old_item";
    static final String NEW_ITEM = "new_item";
    static final String DELETE = "delete";

    I oldItem;

    private TextView dateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        toolbar.inflateMenu(R.menu.add_item);
        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.add_item_menu_delete) {

                Intent intent = new Intent();
                intent.putExtra(OLD_ITEM, oldItem);
                intent.putExtra(DELETE, true);

                setResult(RESULT_OK, intent);
                finish();
            }
            return false;
        });

        oldItem = getIntent().getParcelableExtra(OLD_ITEM);

        dateView = findViewById(R.id.item_edit_date);
        dateView.setText(DateUtil.displayDate(this, oldItem.getDate(), DatabaseHelper.DATE_FORMAT, DateUtil.TECHNICAL_DATE));

        fillFields(oldItem.getName(), oldItem.getCategory(), oldItem.getNotes(), oldItem.getQuantity(), false);

        finishButton.setOnClickListener(v -> {

            if (!nameView.getText().toString().trim().isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra(DELETE, false);
                intent.putExtra(OLD_ITEM, oldItem);
                intent.putExtra(NEW_ITEM, getNewItem());

                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                AnimationUtil.scaleHighlight(errorView);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(SPINNER_POSITION, categoryView.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        categoryView.setSelection(savedInstanceState.getInt(SPINNER_POSITION));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    void toggleError(boolean error) {

        super.toggleError(error);
        if (dateView.getVisibility() == (error ? View.VISIBLE : View.INVISIBLE)) {
            AnimationUtil.push(dateView, !error, false);
        }
    }

    @Override
    int getToolbarTitleResource() {

        return R.string.title_edit_item;
    }

    /**
     * @return an instance of the new item
     */
    abstract I getNewItem();
}
