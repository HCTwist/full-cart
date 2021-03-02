package uk.henrytwist.fullcart.data.shoppingitems

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.NewShoppingItem
import uk.henrytwist.fullcart.models.ShoppingItemCount
import java.time.LocalDateTime

interface ShoppingItemLocalSource {

    fun getCounts(): Flow<List<ShoppingItemCount>>

    fun getSummariesFor(listId: Int): Flow<List<ShoppingItemSummaryModel>>

    suspend fun add(item: NewShoppingItem)

    suspend fun setChecked(id: Int, checked: Boolean, time: LocalDateTime?)

    suspend fun removeAll(id: List<Int>)

    suspend fun removeAllFrom(listId: Int)

    suspend fun getCheckedTimes(): List<Pair<Int, LocalDateTime>>

    suspend fun get(id: Int): ShoppingItemDetailsModel

    suspend fun edit(model: ShoppingItemDetailsModel)

    suspend fun moveToList(model: ShoppingItemSummaryModel, newListId: Int)

    suspend fun removeCategory(categoryId: Int)
}