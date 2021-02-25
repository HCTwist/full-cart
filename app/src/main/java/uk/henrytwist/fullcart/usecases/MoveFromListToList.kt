package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemSummaryModel
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import javax.inject.Inject

class MoveFromListToList @Inject constructor(
        private val shoppingItemRepository: ShoppingItemRepository
) {

    suspend operator fun invoke(item: ShoppingItemSummary, newListId: Int) {

        shoppingItemRepository.moveToList(item.let {

            ShoppingItemSummaryModel(it.id, it.name, it.category?.id, it.quantity, it.checked)
        }, newListId)
    }
}