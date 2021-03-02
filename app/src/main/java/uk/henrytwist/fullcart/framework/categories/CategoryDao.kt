package uk.henrytwist.fullcart.framework.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("SELECT color FROM CategoryEntity WHERE id = :id")
    suspend fun getColor(id: Int): Int

    @Query("SELECT * FROM CategoryEntity WHERE id = :id")
    suspend fun get(id: Int): CategoryEntity

    @Query("UPDATE CategoryEntity SET isDefault = 0 WHERE isDefault")
    suspend fun removeDefault()

    @Transaction
    suspend fun removeDefaultAndInsert(categoryEntity: CategoryEntity) {

        if (categoryEntity.isDefault) {

            removeDefault()
        }
        insert(categoryEntity)
    }

    @Query("UPDATE CategoryEntity SET name = :name, color = :color, isDefault = :isDefault WHERE id = :id")
    suspend fun update(id: Int, name: String, color: Int, isDefault: Boolean)

    @Query("DELETE FROM CategoryEntity WHERE id = :id")
    suspend fun delete(id: Int)
}