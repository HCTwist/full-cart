package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import javax.inject.Inject

class DeletePantry @Inject constructor(
        private val currentListRepository: CurrentListRepository,
        private val listMetaRepository: ListMetaRepository,
        private val pantryItemRepository: PantryItemRepository
) {

    suspend operator fun invoke(id: Int) {

        currentListRepository.set(null)
        pantryItemRepository.removeAllFrom(id)
        listMetaRepository.remove(id)
    }
}