package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.currentlist.CurrentListRepository
import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.models.ListScreen
import javax.inject.Inject

class FindStartingList @Inject constructor(
        private val currentListRepository: CurrentListRepository,
        private val listMetaRepository: ListMetaRepository
) {

    suspend operator fun invoke(): ListScreen? {

        val currentListId = currentListRepository.get()

        return if (currentListId != null) {

            val screen = listMetaRepository.get(currentListId)
            if (screen == null) {

                chooseStartScreen()
            } else {

                ListScreen(screen.type, screen.id)
            }
        } else {

            chooseStartScreen()
        }
    }

    private suspend fun chooseStartScreen(): ListScreen? {

        // TODO Optimise
        return listMetaRepository.getSummaries().getOrNull(0)?.let {

            ListScreen(it.type, it.id)
        }
    }
}