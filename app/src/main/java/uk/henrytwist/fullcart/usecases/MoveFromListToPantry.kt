package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import javax.inject.Inject

class MoveFromListToPantry @Inject constructor(
        private val shoppingItemRepository: ShoppingItemRepository,
        private val pantryItemRepository: PantryItemRepository
) {

    suspend operator fun invoke(items: List<ShoppingItemSummary>, pantryId: Int) {

        val newPantryItems = items.map {

            NewPantryItem(pantryId, it.name, it.category, it.quantity, null)
        }
        pantryItemRepository.addAll(newPantryItems)

        shoppingItemRepository.removeAll(items.map { it.id })
    }
}