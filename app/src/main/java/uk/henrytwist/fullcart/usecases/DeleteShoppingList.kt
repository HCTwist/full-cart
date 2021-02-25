package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import javax.inject.Inject

class DeleteShoppingList @Inject constructor(
        private val currentListRepository: CurrentListRepository,
        private val listMetaRepository: ListMetaRepository,
        private val shoppingItemRepository: ShoppingItemRepository
) {

    suspend operator fun invoke(id: Int) {

        currentListRepository.set(null)
        shoppingItemRepository.removeAllFrom(id)
        listMetaRepository.remove(id)
    }
}