package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.Intent;

/**
 * Implementation of {@link ListItemActivityEdit} for shopping items. This class should not be used
 * manually and should use {@link #buildIntent(Context, ListItem)}
 */
public class ListItemActivityEditShopping extends ListItemActivityEdit<ListItemShopping> {

    static Intent buildIntent(Context context, ListItem item) {

        Intent intent = new Intent(context, ListItemActivityEditShopping.class);
        intent.putExtra(OLD_ITEM, item);

        return intent;
    }

    @Override
    ListItemShopping getNewItem() {

        return new ListItemShopping(getName(), getCategory(), getNotes(), getQuantity(), DateUtil.formatCurrentDate(), oldItem.isChecked());
    }
}
