package uk.henrytwist.fullcart.data.shoppingitems

import uk.henrytwist.fullcart.models.Quantity

class ShoppingItemDetailsModel(val id: Int, val listId: Int, val name: String, val category: Int?, val quantity: Quantity, val checked: Boolean)