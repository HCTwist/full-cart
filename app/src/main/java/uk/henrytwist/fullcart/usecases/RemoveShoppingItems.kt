package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import javax.inject.Inject

class RemoveShoppingItems @Inject constructor(
        private val shoppingItemRepository: ShoppingItemRepository
) {

    suspend operator fun invoke(items: List<ShoppingItemSummary>) {

        shoppingItemRepository.removeAll(items.map { it.id })
    }
}