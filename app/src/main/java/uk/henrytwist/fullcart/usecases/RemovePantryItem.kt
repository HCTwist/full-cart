package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.models.PantryItemDetails
import javax.inject.Inject

class RemovePantryItem @Inject constructor(private val pantryItemRepository: PantryItemRepository) {

    suspend operator fun invoke(item: PantryItemDetails) {

        pantryItemRepository.removeAll(listOf(item.id))
    }
}