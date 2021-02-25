package uk.henrytwist.fullcart.data.pantryitems

import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.UseByDate

class PantryItemDetailsModel(val id: Int, val listId: Int, val name: String, val category: Int?, val quantity: Quantity, val useByDate: UseByDate?)