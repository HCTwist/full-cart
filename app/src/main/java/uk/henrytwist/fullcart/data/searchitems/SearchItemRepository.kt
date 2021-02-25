package uk.henrytwist.fullcart.data.searchitems

import uk.henrytwist.fullcart.framework.searchitems.SearchItemSummaryModel
import uk.henrytwist.fullcart.models.SearchItem
import javax.inject.Inject

class SearchItemRepository @Inject constructor(private val localSource: SearchItemLocalSource){

    suspend fun addOrUpdate(searchItem: SearchItem) {

        localSource.addOrUpdate(searchItem)
    }

    suspend fun search(query: String): List<SearchItemModel> {

        return localSource.search(query)
    }

    suspend fun getMostRecent(n: Int): List<SearchItemSummaryModel> {

        return localSource.getMostRecent(n)
    }
}