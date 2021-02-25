package uk.henrytwist.fullcart.models

class ShoppingItemDetails(id: Int, val listId: Int, name: String, category: Category?, quantity: Quantity, checked: Boolean) : ShoppingItemSummary(id, name, category, quantity, checked)