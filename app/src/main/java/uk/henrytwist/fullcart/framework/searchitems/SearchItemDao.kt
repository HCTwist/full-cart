package uk.henrytwist.fullcart.framework.searchitems

import androidx.room.*

@Dao
interface SearchItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(searchItem: SearchItemEntity): Long

    @Query("UPDATE SearchItemEntity SET count = count + 1, lastUpdated = :time WHERE name = :name AND category = :category")
    suspend fun update(name: String, category: Int, time: Long)

    @Query("SELECT DISTINCT * FROM SearchItemEntity JOIN SearchItemFTSEntity ON SearchItemEntity.name = SearchItemFTSEntity.name WHERE SearchItemFTSEntity MATCH :query")
    suspend fun search(query: String): List<SearchItemEntity>

    @Query("SELECT name, category FROM SearchItemEntity ORDER BY lastUpdated DESC LIMIT :limit")
    suspend fun getMostRecent(limit: Int): List<SearchItemSummaryModel>

    @Query("DELETE FROM SearchItemEntity WHERE category = :categoryId")
    suspend fun deleteWithCategory(categoryId: Int)

    @Transaction
    suspend fun insertOrUpdate(name: String, category: Int, time: Long) {

        val inserted = insert(SearchItemEntity(name, category, 1, time))
        if (inserted == -1L) update(name, category, time)
    }
}