package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import javax.inject.Inject

class SetCurrentList @Inject constructor(private val currentListRepository: CurrentListRepository) {

    suspend operator fun invoke(listId: Int) {

        currentListRepository.set(listId)
    }
}