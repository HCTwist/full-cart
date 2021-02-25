package uk.henrytwist.fullcart.framework.shoppingitems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.data.shoppingitems.CheckedTime
import uk.henrytwist.fullcart.models.ShoppingItemCount

@Dao
interface ShoppingItemDao {

    @Query("SELECT listId, COUNT(*) as count FROM ShoppingItemEntity WHERE NOT checked GROUP BY listId")
    fun getCounts(): Flow<List<ShoppingItemCount>>

    @Query("SELECT id, name, category, quantityNumber, quantityUnit, checked FROM ShoppingItemEntity WHERE listId = :listId")
    fun getSummariesFor(listId: Int): Flow<List<ShoppingItemSummaryEntity>>

    @Insert
    suspend fun insert(item: ShoppingItemEntity)

    @Query("UPDATE ShoppingItemEntity SET checked = :checked, checkedTime = :checkedTime WHERE id = :id")
    suspend fun setChecked(id: Int, checked: Boolean, checkedTime: Long?)

    @Query("DELETE FROM ShoppingItemEntity WHERE id IN (:ids)")
    suspend fun removeAll(ids: List<Int>)

    @Query("DELETE FROM ShoppingItemEntity WHERE listId = :listId")
    suspend fun removeAllFrom(listId: Int)

    @Query("SELECT id, checkedTime FROM ShoppingItemEntity WHERE checked")
    suspend fun getCheckedTimes(): List<CheckedTime>

    @Query("SELECT id, listId, name, category, quantityNumber, quantityUnit, checked FROM ShoppingItemEntity WHERE id = :id")
    suspend fun get(id: Int): ShoppingItemDetailsEntity

    @Query("UPDATE ShoppingItemEntity SET listId = :listId, name = :name, category = :category, quantityNumber = :quantityNumber, quantityUnit = :quantityUnit WHERE id = :id")
    suspend fun update(id: Int, listId: Int, name: String, category: Int?, quantityNumber: Int, quantityUnit: Int)

    @Query("UPDATE ShoppingItemEntity SET listId = :listId WHERE id = :id")
    suspend fun updateList(id: Int, listId: Int)
}