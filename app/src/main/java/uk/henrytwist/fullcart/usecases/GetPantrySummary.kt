package uk.henrytwist.fullcart.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.models.PantrySummary
import java.time.LocalDate
import javax.inject.Inject

class GetPantrySummary @Inject constructor(private val pantryItemRepository: PantryItemRepository, private val settingsRepository: SettingsRepository) {

    operator fun invoke(): Flow<PantrySummary> {

        return pantryItemRepository.getUseByDates().map { dates ->

            val now = LocalDate.now()
            val useSoonThreshold = settingsRepository.getUseSoonThreshold()

            val toUseSoonCount = dates.count {

                it.isToUseSoon(now, useSoonThreshold)
            }

            PantrySummary(toUseSoonCount)
        }
    }
}