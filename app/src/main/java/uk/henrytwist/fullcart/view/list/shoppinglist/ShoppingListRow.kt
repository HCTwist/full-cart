package uk.henrytwist.fullcart.view.list.shoppinglist

import uk.henrytwist.fullcart.models.ShoppingItemSummary

sealed class ShoppingListRow {

    class Item(val item: ShoppingItemSummary) : ShoppingListRow()

    class Divider(val showMoveToPantry: Boolean, val singlePantryName: String?) : ShoppingListRow()
}