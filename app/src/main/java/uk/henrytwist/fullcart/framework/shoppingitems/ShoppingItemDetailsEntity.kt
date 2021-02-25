package uk.henrytwist.fullcart.framework.shoppingitems

class ShoppingItemDetailsEntity(
        val id: Int,
        val listId: Int,
        val name: String,
        val category: Int?,
        val quantityNumber: Int,
        val quantityUnit: Int,
        val checked: Boolean
)