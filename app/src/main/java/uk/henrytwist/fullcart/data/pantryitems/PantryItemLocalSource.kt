package uk.henrytwist.fullcart.data.pantryitems

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.models.UseByDate

interface PantryItemLocalSource {

    fun getUseByDates(): Flow<List<UseByDate>>

    fun getCount(): Flow<Int>

    fun getSummariesFor(listId: Int): Flow<List<PantryItemSummaryModel>>

    suspend fun add(item: NewPantryItem)

    suspend fun addAll(items: List<NewPantryItem>)

    suspend fun removeAll(ids: List<Int>)

    suspend fun removeAllFrom(listId: Int)

    suspend fun getUseByItems(): List<PantryUseByItemModel>

    suspend fun setQuantityNumber(id: Int, n: Int)

    suspend fun get(id: Int): PantryItemDetailsModel

    suspend fun edit(item: PantryItemDetailsModel)

    suspend fun move(id: Int, listId: Int)
}