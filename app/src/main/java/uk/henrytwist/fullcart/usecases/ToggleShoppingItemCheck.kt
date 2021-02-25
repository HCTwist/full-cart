package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import java.time.LocalDateTime
import javax.inject.Inject

class ToggleShoppingItemCheck @Inject constructor(private val shoppingItemRepository: ShoppingItemRepository) {

    suspend operator fun invoke(item: ShoppingItemSummary) {

        val time = if (item.checked) null else LocalDateTime.now()
        shoppingItemRepository.setChecked(item.id, !item.checked, time)
    }
}