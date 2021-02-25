package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.data.shoppingitems.ShoppingItemRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CleanupCheckedItems @Inject constructor(
        private val settingsRepository: SettingsRepository,
        private val shoppingItemRepository: ShoppingItemRepository
) {

    suspend operator fun invoke() {

        val days = settingsRepository.getCheckedItemAutoDelete()

        if (days > 0) {

            val checkedTimes = shoppingItemRepository.getCheckedTimes()

            val now = LocalDateTime.now()
            val thresholdDateTime = now.minusDays(days.toLong())
            val toDelete = ArrayList<Int>(checkedTimes.size)

            checkedTimes.forEach {

                if (it.second.isBefore(thresholdDateTime)) {

                    toDelete.add(it.first)
                }
            }

            shoppingItemRepository.removeAll(toDelete)
        }
    }
}