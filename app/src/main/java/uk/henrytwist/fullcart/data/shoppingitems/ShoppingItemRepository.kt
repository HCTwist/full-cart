package uk.henrytwist.fullcart.data.shoppingitems

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.NewShoppingItem
import uk.henrytwist.fullcart.models.ShoppingItemCount
import java.time.LocalDateTime
import javax.inject.Inject

class ShoppingItemRepository @Inject constructor(private val localSource: ShoppingItemLocalSource) {

    fun getItemCounts(): Flow<List<ShoppingItemCount>> {

        return localSource.getCounts()
    }

    fun getAllFor(listId: Int): Flow<List<ShoppingItemSummaryModel>> {

        return localSource.getSummariesFor(listId)
    }

    suspend fun add(item: NewShoppingItem) {

        localSource.add(item)
    }

    suspend fun setChecked(id: Int, checked: Boolean, time: LocalDateTime?) {

        localSource.setChecked(id, checked, time)
    }

    suspend fun removeAll(ids: List<Int>) {

        localSource.removeAll(ids)
    }

    suspend fun removeAllFrom(listId: Int) {

        localSource.removeAllFrom(listId)
    }

    suspend fun getCheckedTimes(): List<Pair<Int, LocalDateTime>> {

        return localSource.getCheckedTimes()
    }

    suspend fun get(id: Int): ShoppingItemDetailsModel {

        return localSource.get(id)
    }

    suspend fun edit(item: ShoppingItemDetailsModel) {

        localSource.edit(item)
    }

    suspend fun moveToList(item: ShoppingItemSummaryModel, newListId: Int) {

        localSource.moveToList(item, newListId)
    }
}