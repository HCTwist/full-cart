package uk.henrytwist.fullcart.models

open class PantryItemSummary(val id: Int, val name: String, val category: Category?, val quantity: Quantity, val useByDate: UseByDate?, val toUseSoon: Boolean) {

    fun hasUseByDate() = useByDate != null
}