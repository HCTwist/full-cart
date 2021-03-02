package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.searchitems.SearchItemRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import javax.inject.Inject

class RemoveCategory @Inject constructor(
        private val categoryRepository: CategoryRepository,
        private val shoppingItemRepository: ShoppingItemRepository,
        private val searchItemRepository: SearchItemRepository,
        private val pantryItemRepository: PantryItemRepository
) {

    suspend operator fun invoke(id: Int) {

        shoppingItemRepository.removeCategory(id)
        pantryItemRepository.removeCategory(id)
        searchItemRepository.removeWithCategory(id)
        categoryRepository.remove(id)
    }
}