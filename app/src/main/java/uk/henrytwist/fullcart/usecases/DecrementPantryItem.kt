package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.models.PantryItemSummary
import javax.inject.Inject

class DecrementPantryItem @Inject constructor(private val pantryItemRepository: PantryItemRepository) {

    suspend operator fun invoke(item: PantryItemSummary) {

        if (item.quantity.canDecrement()) {

            pantryItemRepository.setQuantityNumber(item.id, item.quantity.decremented())
        } else {

            pantryItemRepository.removeAll(listOf(item.id))
        }
    }
}