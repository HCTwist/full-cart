package uk.henrytwist.fullcart.data.searchitems

import uk.henrytwist.fullcart.framework.searchitems.SearchItemSummaryModel
import uk.henrytwist.fullcart.models.SearchItem

interface SearchItemLocalSource {

    suspend fun addOrUpdate(item: SearchItem)

    suspend fun search(query: String): List<SearchItemModel>

    suspend fun getMostRecent(limit: Int): List<SearchItemSummaryModel>
}