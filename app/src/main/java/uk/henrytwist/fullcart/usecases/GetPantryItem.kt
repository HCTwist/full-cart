package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.models.PantryItemDetails
import javax.inject.Inject

class GetPantryItem @Inject constructor(private val pantryItemRepository: PantryItemRepository, private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(id: Int): PantryItemDetails {

        val m = pantryItemRepository.get(id)
        val category = m.category?.let { categoryRepository.get(it) }
        return PantryItemDetails(m.id, m.listId, m.name, category, m.quantity, m.useByDate)
    }
}