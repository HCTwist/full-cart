package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemDetailsModel
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.ShoppingItemDetails
import javax.inject.Inject

class EditShoppingItem @Inject constructor(private val shoppingItemRepository: ShoppingItemRepository) {

    suspend operator fun invoke(editedItem: ShoppingItemDetails) {

        shoppingItemRepository.edit(editedItem.let {

            ShoppingItemDetailsModel(it.id, it.listId, it.name, it.category?.id, it.quantity, it.checked)
        })
    }
}