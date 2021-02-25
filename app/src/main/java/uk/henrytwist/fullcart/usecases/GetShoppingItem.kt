package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.ShoppingItemDetails
import javax.inject.Inject

class GetShoppingItem @Inject constructor(private val shoppingItemRepository: ShoppingItemRepository, private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(id: Int): ShoppingItemDetails {

        val m = shoppingItemRepository.get(id)
        val category = m.category?.let { categoryRepository.get(it) }
        return ShoppingItemDetails(m.id, m.listId, m.name, category, m.quantity, m.checked)
    }
}