package uk.henrytwist.fullcart.framework.pantryitems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PantryItemDao {

    @Query("SELECT useByDate FROM PantryItemEntity WHERE useByDate IS NOT NULL")
    fun getUseByDates(): Flow<List<Long>>

    @Query("SELECT COUNT(*) FROM PantryItemEntity")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM PantryItemEntity WHERE listId = :listId")
    fun getSummariesFor(listId: Int): Flow<List<PantryItemEntity>>

    @Insert
    suspend fun add(item: PantryItemEntity)

    @Insert
    suspend fun addAll(items: List<PantryItemEntity>)

    @Query("DELETE FROM PantryItemEntity WHERE id IN (:ids)")
    suspend fun deleteAll(ids: List<Int>)

    @Query("DELETE FROM PantryItemEntity WHERE listId = :listId")
    suspend fun deleteAllFrom(listId: Int)

    @Query("SELECT listId, name, useByDate FROM PantryItemEntity WHERE useByDate NOT NULL")
    suspend fun getUseByItems(): List<PantryNotificationItemEntity>

    @Query("UPDATE PantryItemEntity SET quantityNumber = :quantityNumber WHERE id = :id")
    suspend fun updateQuantityNumber(id: Int, quantityNumber: Int)

    @Query("SELECT * FROM PantryItemEntity WHERE id = :id")
    suspend fun get(id: Int): PantryItemEntity

    @Query("UPDATE PantryItemEntity SET listId = :listId, name = :name, category = :category, quantityNumber = :quantityNumber, quantityUnit = :quantityUnit, useByDate = :useByDate WHERE id = :id")
    suspend fun edit(id: Int, listId: Int, name: String, category: Int?, quantityNumber: Int, quantityUnit: Int, useByDate: Long?)

    @Query("UPDATE PantryItemEntity SET listId = :listId WHERE id = :id")
    suspend fun move(id: Int, listId: Int)

    @Query("UPDATE PantryItemEntity SET category = NULL WHERE category = :categoryId")
    suspend fun removeCategory(categoryId: Int)
}