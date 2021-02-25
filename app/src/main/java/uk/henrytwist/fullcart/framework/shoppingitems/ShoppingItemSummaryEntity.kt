package uk.henrytwist.fullcart.framework.shoppingitems

class ShoppingItemSummaryEntity(
        val id: Int,
        val name: String,
        val category: Int?,
        val quantityNumber: Int,
        val quantityUnit: Int,
        val checked: Boolean
)