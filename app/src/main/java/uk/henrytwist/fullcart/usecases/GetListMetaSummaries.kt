package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.ListMetaSummary
import javax.inject.Inject

class GetListMetaSummaries @Inject constructor(private val listMetaRepository: ListMetaRepository) {

    suspend operator fun invoke(): List<ListMetaSummary> {

        return listMetaRepository.getSummaries()
    }
}