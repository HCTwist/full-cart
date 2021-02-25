package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import javax.inject.Inject

class GetCurrentList @Inject constructor(private val currentListRepository: CurrentListRepository) {

    suspend operator fun invoke(): Int? {

        return currentListRepository.get()
    }
}