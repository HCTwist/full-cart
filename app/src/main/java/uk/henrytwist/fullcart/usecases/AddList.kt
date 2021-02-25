package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.NewList
import javax.inject.Inject

class AddList @Inject constructor(private val listMetaRepository: ListMetaRepository) {

    suspend operator fun invoke(newList: NewList): Int {

        return listMetaRepository.add(newList)
    }
}