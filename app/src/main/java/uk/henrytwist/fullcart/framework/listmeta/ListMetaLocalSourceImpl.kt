package uk.henrytwist.fullcart.framework.listmeta

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.fullcart.data.listmeta.ListMetaLocalSource
import uk.henrytwist.fullcart.models.ListMeta
import uk.henrytwist.fullcart.models.ListMetaSummary
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.models.NewList
import javax.inject.Inject

class ListMetaLocalSourceImpl @Inject constructor(private val dao: ListMetaDao) : ListMetaLocalSource {

    override suspend fun get(id: Int): ListMeta? {

        return dao.get(id)?.let {

            ListMeta(it.id, it.name, resolveType(it.type))
        }
    }

    override suspend fun getSummaries(): List<ListMetaSummary> {

        return dao.getSummaries().map {

            ListMetaSummary(it.id, it.name, resolveType(it.type))
        }
    }

    override fun getName(id: Int): Flow<String> {

        return dao.getName(id)
    }

    override suspend fun add(list: NewList): Int {

        return dao.insert(ListMetaEntity(0, list.name, list.type.ordinal)).toInt()
    }

    override suspend fun remove(id: Int) {

        dao.delete(id)
    }

    private fun resolveType(ordinal: Int) = ListType.values()[ordinal]
}