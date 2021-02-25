package uk.henrytwist.fullcart.data.listmeta

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.models.NewList
import uk.henrytwist.fullcart.models.ListMeta
import uk.henrytwist.fullcart.models.ListMetaSummary
import javax.inject.Inject

class ListMetaRepository @Inject constructor(private val localSource: ListMetaLocalSource) {

    suspend fun getSummaries(): List<ListMetaSummary> {

        return localSource.getSummaries()
    }

    suspend fun get(id: Int): ListMeta? {

        return localSource.get(id)
    }

    fun getName(id: Int): Flow<String> {

        return localSource.getName(id)
    }

    suspend fun add(list: NewList): Int {

        return localSource.add(list)
    }

    suspend fun remove(id: Int) {

        localSource.remove(id)
    }
}