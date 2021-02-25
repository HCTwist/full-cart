package uk.henrytwist.fullcart.data.pantryitems

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.models.UseByDate
import javax.inject.Inject

class PantryItemRepository @Inject constructor(private val localSource: PantryItemLocalSource) {

    fun getUseByDates(): Flow<List<UseByDate>> {

        return localSource.getUseByDates()
    }

    fun getCount(): Flow<Int> {

        return localSource.getCount()
    }

    fun getSummariesFor(listId: Int): Flow<List<PantryItemSummaryModel>> {

        return localSource.getSummariesFor(listId)
    }

    suspend fun add(item: NewPantryItem) {

        localSource.add(item)
    }

    suspend fun addAll(items: List<NewPantryItem>) {

        localSource.addAll(items)
    }

    suspend fun removeAll(ids: List<Int>) {

        localSource.removeAll(ids)
    }

    suspend fun removeAllFrom(listId: Int) {

        localSource.removeAllFrom(listId)
    }

    suspend fun getUseByItems(): List<PantryUseByItemModel> {

        return localSource.getUseByItems()
    }

    suspend fun setQuantityNumber(id: Int, n: Int) {

        localSource.setQuantityNumber(id, n)
    }

    suspend fun get(id: Int): PantryItemDetailsModel {

        return localSource.get(id)
    }

    suspend fun edit(item: PantryItemDetailsModel) {

        localSource.edit(item)
    }

    suspend fun move(id: Int, listId: Int) {

        localSource.move(id, listId)
    }
}