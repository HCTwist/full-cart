package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.ListMeta
import javax.inject.Inject

class GetListMeta @Inject constructor(private val listMetaRepository: ListMetaRepository) {

    suspend operator fun invoke(id: Int): ListMeta? {

        return listMetaRepository.get(id)
    }
}