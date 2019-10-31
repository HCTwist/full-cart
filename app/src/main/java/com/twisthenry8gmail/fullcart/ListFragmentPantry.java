package com.twisthenry8gmail.fullcart;

import android.content.Intent;

import com.twisthenry8gmail.roundedbottomsheet.BottomMenuFragment;

import java.util.ArrayList;

/**
 * Implementation of {@link ListFragment} for the pantry
 */
public class ListFragmentPantry extends ListFragment<ModelPantry, ListItemPantry> {

    static final String OVERFLOW_MENU_TAG = "pantry_overflow";

    @Override
    int getLayoutResource() {

        return R.layout.fragment_pantry;
    }

    @Override
    int getListResource() {

        return R.id.pantry_list;
    }

    @Override
    ModelAdapter<ModelPantry, ListItemPantry> createAdapter() {

        return new ModelAdapterPantry(getContext(), this, () -> ListFragmentPantry.this);
    }

    @Override
    int getLoadingViewResource() {

        return R.id.pantry_loading;
    }

    @Override
    int getEmptyViewResource() {

        return R.id.pantry_empty;
    }

    @Override
    void updateDatabase(ListItemPantry oldItem, ListItemPantry newItem) {

        DatabaseHelper.getInstance(getContext()).getWritableDatabase().update(DatabaseHelper.PANTRY_TABLE_NAME, newItem.toContentValues(), oldItem.getWhereClause(), oldItem.getWhereArgs());
    }

    @Override
    void addToDatabase(ListItemPantry item) {

        DatabaseHelper.getInstance(getContext()).addPantryItem(item);
    }

    @Override
    void deleteFromDatabase(ListItemPantry item) {

        DatabaseHelper.getInstance(getContext()).deletePantryItem(item);
    }

    @Override
    void onSettingChanged(ArrayList<String> keys) {

        if (keys.contains(SettingsFragment.KEY_PANTRY_DATE_FORMAT) || keys.contains(SettingsFragment.KEY_MARK_USE_BY)) {

            if (getAdapter().isLoaded()) {
                for (int i = 0; i < getAdapter().getEntries().size(); i++) {

                    ModelPantry m = getAdapter().getEntries().get(i);

                    if (m.getType() == Model.Type.CONTENT && m.getItem().hasByDate()) {
                        getAdapter().notifyItemChanged(i, getAdapter().new ItemChange(m.getItem()));
                    }
                }
            }
        }
    }

    @Override
    void showOverflowMenu() {

        int[] icons = {
                R.drawable.outline_notifications_24,
                R.drawable.outline_feedback_24
        };
        int[] texts = {
                R.string.menu_show_notification,
                R.string.menu_feedback
        };

        BottomMenuFragment menuFragment = BottomMenuFragment.getInstance(icons, texts, R.color.body, R.color.accent);
        if (getFragmentManager() != null) {
            menuFragment.show(getFragmentManager(), OVERFLOW_MENU_TAG);
        }
    }

    @Override
    void menuClick(int position) {

        switch (position) {
            case 0:
                if (getContext() != null) {
                    UseByReceiver.showNotification(getContext(), 5, false);
                }
                break;
            case 1:
                if (getContext() != null) {
                    FeedbackUtil.startFeedback(getContext(), FeedbackUtil.MENU_FEEDBACK_SUBJECT);
                }
                break;
        }
    }

    @Override
    public void onFABClick() {

        Intent intent = new Intent(getContext(), ListItemActivityAddPantry.class);
        startActivityForResult(intent, RequestCodes.REQUEST_ITEM_ADD);
    }

    @Override
    public boolean onFABLongClick() {

        return false;
    }
}
