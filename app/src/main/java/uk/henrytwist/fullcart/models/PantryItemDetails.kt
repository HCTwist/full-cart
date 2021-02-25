package uk.henrytwist.fullcart.models

class PantryItemDetails(
        val id: Int,
        val listId: Int,
        val name: String,
        val category: Category?,
        val quantity: Quantity,
        val useByDate: UseByDate?
)