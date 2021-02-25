package uk.henrytwist.fullcart.framework.searchitems

import uk.henrytwist.fullcart.data.searchitems.SearchItemLocalSource
import uk.henrytwist.fullcart.data.searchitems.SearchItemModel
import uk.henrytwist.fullcart.models.SearchItem
import java.time.Instant
import javax.inject.Inject

class SearchItemLocalSourceImpl @Inject constructor(private val searchItemDao: SearchItemDao) : SearchItemLocalSource {

    override suspend fun addOrUpdate(item: SearchItem) {

        val categoryId = item.category?.id ?: -1
        searchItemDao.insertOrUpdate(item.name, categoryId, Instant.now().epochSecond)
    }

    override suspend fun search(query: String): List<SearchItemModel> {

        val results = searchItemDao.search("*$query*")

        return results.map {

            val category = if (it.category == -1) null else it.category
            SearchItemModel(it.name, category, it.count, it.lastUpdated)
        }
    }

    override suspend fun getMostRecent(limit: Int): List<SearchItemSummaryModel> {

        return searchItemDao.getMostRecent(limit).map {

            val category = if (it.category == -1) null else it.category
            SearchItemSummaryModel(it.name, category)
        }
    }
}