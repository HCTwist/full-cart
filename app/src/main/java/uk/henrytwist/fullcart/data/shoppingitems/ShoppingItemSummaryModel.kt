package uk.henrytwist.fullcart.data.shoppingitems

import uk.henrytwist.fullcart.models.Quantity

class ShoppingItemSummaryModel(val id: Int, val name: String, val category: Int?, val quantity: Quantity, val checked: Boolean)