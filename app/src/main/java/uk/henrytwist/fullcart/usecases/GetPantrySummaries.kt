package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.ListMetaSummary
import uk.henrytwist.fullcart.models.ListType
import javax.inject.Inject

class GetPantrySummaries @Inject constructor(private val listMetaRepository: ListMetaRepository) {

    suspend operator fun invoke(): List<ListMetaSummary> {

        return listMetaRepository.getSummaries().filter { it.type == ListType.PANTRY }
    }
}