package com.twisthenry8gmail.fullcart;

import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;
import com.twisthenry8gmail.roundedbottomsheet.BottomMenuFragment;

import java.util.ArrayList;

/**
 * Implementation of {@link ListFragment} for the shopping list
 */
public class ListFragmentShopping extends ListFragment<ModelShopping, ListItemShopping> {

    static final String OVERFLOW_MENU_TAG = "shopping_overflow";

    @Override
    int getLayoutResource() {

        return R.layout.fragment_shopping;
    }

    @Override
    int getListResource() {

        return R.id.shopping_list;
    }

    @Override
    ModelAdapter<ModelShopping, ListItemShopping> createAdapter() {

        return new ModelAdapterShopping(getContext(), this, (ShoppingContentViewHolder.TransferInterface) getActivity(), () -> ListFragmentShopping.this);
    }

    @Override
    int getLoadingViewResource() {

        return R.id.list_loading;
    }

    @Override
    int getEmptyViewResource() {

        return R.id.shopping_empty;
    }

    @Override
    void updateDatabase(ListItemShopping oldItem, ListItemShopping newItem) {

        DatabaseHelper.getInstance(getContext()).getWritableDatabase().update(DatabaseHelper.LIST_TABLE_NAME, newItem.toContentValues(), oldItem.getWhereClause(), oldItem.getWhereArgs());
    }

    @Override
    void addToDatabase(ListItemShopping item) {

        DatabaseHelper.getInstance(getContext()).addShoppingItem(item);
    }

    @Override
    void deleteFromDatabase(ListItemShopping item) {

        DatabaseHelper.getInstance(getContext()).deleteShoppingItem(item);
    }

    @Override
    void onSettingChanged(ArrayList<String> keys) {

        if (keys.contains(SettingsFragment.KEY_AUTO_DELETE_CHECKED)) {

            for (int i = 0; i < getAdapter().getEntries().size(); i++) {

                ModelShopping m = getAdapter().getEntries().get(i);

                if (m.getType() == Model.Type.CONTENT && m.getItem().isChecked()) {

                    int autoDelete = SettingsFragment.getCheckedAutoDelete(getContext());
                    if (autoDelete != 0 && -DateUtil.getCurrentDateDifference(m.getItem().getDate()) >= autoDelete) {

                        removeItem(m.getItem());
                    }
                }
            }
        }
    }

    @Override
    void showOverflowMenu() {

        int[] icons = {
                R.drawable.outline_delete_sweep_24,
                R.drawable.outline_remove_shopping_cart_24,
                R.drawable.outline_feedback_24
        };
        int[] texts = {
                R.string.menu_delete_checked,
                R.string.menu_transfer_checked,
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
                removeCheckedItems();
                break;
            case 1:
                transferCheckedItems();
                break;
            case 2:
                if (getContext() != null) {
                    FeedbackUtil.startFeedback(getContext(), FeedbackUtil.MENU_FEEDBACK_SUBJECT);
                }
                break;
        }
    }

    @Override
    public void onFABClick() {

        Intent intent = new Intent(getContext(), ListItemActivityAddShopping.class);
        startActivityForResult(intent, RequestCodes.REQUEST_ITEM_ADD);
    }

    @Override
    public boolean onFABLongClick() {

        return false;
    }

    /**
     * Transfers an item from this fragment to the {@link ListFragmentPantry}
     *
     * @param pantryFragment the pantry fragment
     * @param listItem       the item to transfer
     * @param pantryItem     the new pantry item
     */
    void transfer(ListFragmentPantry pantryFragment, ListItemShopping listItem, ListItemPantry pantryItem) {

        if (getActivity() != null) {

            removeItem(listItem);

            ArrayList<ListItemPantry> items = new ArrayList<>();
            items.add(pantryItem);
            pantryFragment.addItems(items, false);
        }
    }

    /**
     * Reverses an item transfer
     *
     * @see #transfer(ListFragmentPantry, ListItemShopping, ListItemPantry)
     */
    void undoTransfer(ListFragmentPantry pantryFragment, ListItemShopping listItem, ListItemPantry pantryItem) {

        if (getActivity() != null) {

            pantryFragment.removeItem(pantryItem);

            ArrayList<ListItemShopping> items = new ArrayList<>();
            items.add(listItem);
            addItems(items, false);
            ((ModelAdapterShopping) getAdapter()).ensureBasketHeader();
        }
    }

    /**
     * Removes all checked items from the database and adapter
     */
    private void removeCheckedItems() {

        DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
        helper.getWritableDatabase().delete(DatabaseHelper.LIST_TABLE_NAME, DatabaseHelper.LIST_CHECKED + " = 1", null);

        ArrayList<ListItemShopping> removeQueue = new ArrayList<>();
        for (ModelShopping model : getAdapter().getEntries()) {

            if (model.getType() == Model.Type.CONTENT && model.getItem().isChecked()) {
                removeQueue.add(model.getItem());
            }
        }

        for (ListItemShopping item : removeQueue) {

            getAdapter().removeItem(item);
        }
    }

    /**
     * Transfers all the checked items to the pantry (with blank by dates)
     *
     * @see #transfer(ListFragmentPantry, ListItemShopping, ListItemPantry)
     */
    private void transferCheckedItems() {

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {

            ArrayList<ListItemShopping> queue = new ArrayList<>();

            for (ModelShopping m : getAdapter().getEntries()) {

                if (m.getType() == Model.Type.CONTENT) {
                    ListItemShopping item = m.getItem();
                    if (item.isChecked()) {
                        queue.add(item);
                    }
                }
            }

            if(!queue.isEmpty()) {

                ArrayList<ListItemPantry> buffer = new ArrayList<>();

                for (ListItemShopping itemS : queue) {

                    ListItemPantry itemP = new ListItemPantry(itemS.getName(),
                            itemS.getCategory(),
                            itemS.getNotes(),
                            itemS.getQuantity(),
                            DateUtil.formatCurrentDate(),
                            SettingsFragment.getDefaultByDateType(getContext()),
                            "");
                    buffer.add(itemP);

                    transfer(mainActivity.getPantryFragment(), itemS, itemP);
                }
                mainActivity.showPantryDot();

                Snackbar snackbar = AnchoredSnackbar.make(getView(), R.string.shopping_transfer_prompt_message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, v -> {

                    for (int i = 0; i < queue.size(); i++) {

                        undoTransfer(mainActivity.getPantryFragment(), queue.get(i), buffer.get(i));
                        mainActivity.hidePantryDot();
                    }
                });
                snackbar.show();
            }
        }
    }
}
