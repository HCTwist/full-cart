package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.pantryitems.PantryItemDetailsModel
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.UseByDate
import javax.inject.Inject

class EditPantryItem @Inject constructor(private val pantryItemRepository: PantryItemRepository) {

    suspend operator fun invoke(id: Int, listId: Int, name: String, category: Category?, quantity: Quantity, useByDate: UseByDate?) {

        pantryItemRepository.edit(PantryItemDetailsModel(id, listId, name, category?.id, quantity, useByDate))
    }
}