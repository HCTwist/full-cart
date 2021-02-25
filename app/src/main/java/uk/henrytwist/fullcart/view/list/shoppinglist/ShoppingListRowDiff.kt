package uk.henrytwist.fullcart.view.list.shoppinglist

import androidx.recyclerview.widget.DiffUtil

object ShoppingListRowDiff : DiffUtil.ItemCallback<ShoppingListRow>() {

    override fun areItemsTheSame(oldItem: ShoppingListRow, newItem: ShoppingListRow): Boolean {

        if (oldItem is ShoppingListRow.Divider && newItem is ShoppingListRow.Divider) {

            return areContentsTheSame(oldItem, newItem)
        }

        if (oldItem is ShoppingListRow.Item && newItem is ShoppingListRow.Item) {

            return oldItem.item.id == newItem.item.id
        }

        return false
    }

    override fun areContentsTheSame(oldItem: ShoppingListRow, newItem: ShoppingListRow): Boolean {

        if (oldItem is ShoppingListRow.Item && newItem is ShoppingListRow.Item) {

            val i1 = oldItem.item
            val i2 = newItem.item

            return i1.name == i2.name &&
                    i1.category?.color == i2.category?.color &&
                    i1.quantity == i2.quantity &&
                    i1.checked == i2.checked
        }

        if (oldItem is ShoppingListRow.Divider && newItem is ShoppingListRow.Divider) {

            return oldItem.showMoveToPantry == newItem.showMoveToPantry
        }

        return false
    }
}