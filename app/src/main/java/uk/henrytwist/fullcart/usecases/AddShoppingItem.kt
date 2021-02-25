package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.searchitems.SearchItemRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.NewShoppingItem
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.SearchItem
import javax.inject.Inject

class AddShoppingItem @Inject constructor(private val shoppingItemRepository: ShoppingItemRepository, private val searchItemRepository: SearchItemRepository) {

    suspend operator fun invoke(listId: Int, name: String, category: Category?, quantity: Quantity) {

        shoppingItemRepository.add(NewShoppingItem(listId, name, category, quantity, false, null))
        searchItemRepository.addOrUpdate(SearchItem(name, category))
    }
}