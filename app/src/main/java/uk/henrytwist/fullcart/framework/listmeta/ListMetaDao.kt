package uk.henrytwist.fullcart.framework.listmeta

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListMetaDao {

    @Query("SELECT id, name, type FROM ListMetaEntity WHERE id = :id")
    suspend fun get(id: Int): ListMetaEntity?

    @Query("SELECT id, name, type FROM ListMetaEntity")
    suspend fun getSummaries(): List<ListMetaEntity>

    @Query("SELECT name FROM ListMetaEntity WHERE id = :id")
    fun getName(id: Int): Flow<String>

    @Insert
    suspend fun insert(listMetaEntity: ListMetaEntity): Long

    @Query("DELETE FROM ListMetaEntity WHERE id = :id")
    suspend fun delete(id: Int)
}