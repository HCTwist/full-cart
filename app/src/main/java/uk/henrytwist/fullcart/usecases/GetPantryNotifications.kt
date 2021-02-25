package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.listmeta.ListMetaRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryUseByItemModel
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.models.PantryNotification
import java.time.LocalDate
import javax.inject.Inject

class GetPantryNotifications @Inject constructor(
        private val pantryItemRepository: PantryItemRepository,
        private val listMetaRepository: ListMetaRepository,
        private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): List<PantryNotification> {

        val nPerGroup = 3

        val useByItems = pantryItemRepository.getUseByItems()

        val useSoonThreshold = settingsRepository.getUseSoonThreshold()
        val now = LocalDate.now()

        // We want only use soon items and only n per group
        val modelGroups = mutableMapOf<Int, MutableList<PantryUseByItemModel>>()

        // Split into groups
        useByItems.forEach {

            if (it.useByDate.isToUseSoon(now, useSoonThreshold)) {

                val group = modelGroups[it.listId]
                if (group == null) {

                    modelGroups[it.listId] = mutableListOf(it)
                } else {

                    group.add(it)
                }
            }
        }

        val notifications = ArrayList<PantryNotification>(modelGroups.size)

        // Restrict to nPerGroup and resolve list name
        modelGroups.forEach { modelGroup ->

            val meta = listMetaRepository.get(modelGroup.key)
            if (meta != null) {

                val items = if (modelGroup.value.size <= nPerGroup) {

                    modelGroup.value
                } else {

                    modelGroup.value.sortBy { it.useByDate.date }
                    modelGroup.value.take(nPerGroup)
                }

                val notification = PantryNotification(modelGroup.key, meta.name, items.map {

                    PantryNotification.Item(it.name, it.useByDate)
                })
                notifications.add(notification)
            }
        }

        return notifications
    }
}