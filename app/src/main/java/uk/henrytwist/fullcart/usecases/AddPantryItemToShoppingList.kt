package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.NewShoppingItem
import uk.henrytwist.fullcart.models.PantryItemDetails
import uk.henrytwist.fullcart.models.Quantity
import javax.inject.Inject

class AddPantryItemToShoppingList @Inject constructor(private val shoppingItemRepository: ShoppingItemRepository) {

    suspend operator fun invoke(item: PantryItemDetails, listId: Int) {

        shoppingItemRepository.add(NewShoppingItem(listId, item.name, item.category, Quantity(1, Quantity.Unit.SINGLE), false, null))
    }
}