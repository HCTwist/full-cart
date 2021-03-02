package uk.henrytwist.fullcart.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.kotlinbasics.sortedWithFieldComparator
import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import javax.inject.Inject

class GetShoppingItemSummaries @Inject constructor(
        private val shoppingItemRepository: ShoppingItemRepository,
        private val categoryRepository: CategoryRepository
) {

    operator fun invoke(listId: Int): Flow<List<ShoppingItemSummary>> {

        return shoppingItemRepository.getAllFor(listId).map { list ->

            list.map { model ->

                val category = model.category?.let { categoryRepository.get(it) }
                ShoppingItemSummary(model.id, model.name, category, model.quantity, model.checked)
            }.sortedWithFieldComparator {

                addField { it.category?.name }
                addField { it.name }
            }
        }
    }
}