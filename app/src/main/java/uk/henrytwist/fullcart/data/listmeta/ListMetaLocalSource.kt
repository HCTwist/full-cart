package uk.henrytwist.fullcart.data.listmeta

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.ListMeta
import uk.henrytwist.fullcart.models.ListMetaSummary
import uk.henrytwist.fullcart.models.NewList

interface ListMetaLocalSource {

    suspend fun getSummaries(): List<ListMetaSummary>

    suspend fun get(id: Int): ListMeta?

    fun getName(id: Int): Flow<String>

    suspend fun add(list: NewList): Int

    suspend fun remove(id: Int)
}