package uk.henrytwist.fullcart.models

open class ShoppingItemSummary(val id: Int, val name: String, val category: Category?, val quantity: Quantity, val checked: Boolean)