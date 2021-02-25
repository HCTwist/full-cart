package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.searchitems.SearchItemRepository
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.models.SearchItem
import javax.inject.Inject

class AddPantryItem @Inject constructor(
        private val pantryItemRepository: PantryItemRepository,
        private val searchItemRepository: SearchItemRepository
) {

    suspend operator fun invoke(item: NewPantryItem) {

        pantryItemRepository.add(item)
        searchItemRepository.addOrUpdate(SearchItem(item.name, item.category))
    }
}