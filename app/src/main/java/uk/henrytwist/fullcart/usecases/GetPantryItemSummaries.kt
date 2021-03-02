package uk.henrytwist.fullcart.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.henrytwist.kotlinbasics.sortedWithFieldComparator
import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.pantryitems.PantryItemRepository
import uk.henrytwist.fullcart.data.settings.SettingsRepository
import uk.henrytwist.fullcart.models.PantryItemSummary
import java.time.LocalDate
import javax.inject.Inject

class GetPantryItemSummaries @Inject constructor(
        private val pantryItemRepository: PantryItemRepository,
        private val categoryRepository: CategoryRepository,
        private val settingsRepository: SettingsRepository
) {

    operator fun invoke(listId: Int): Flow<List<PantryItemSummary>> {

        return pantryItemRepository.getSummariesFor(listId).map { list ->

            val now = LocalDate.now()
            val useSoonThreshold = settingsRepository.getUseSoonThreshold()

            list.map {

                val category = if (it.category == null) null else categoryRepository.get(it.category)
                val useSoon = it.useByDate != null && it.useByDate.isToUseSoon(now, useSoonThreshold)

                PantryItemSummary(it.id, it.name, category, it.quantity, it.useByDate, useSoon)
            }.sortedWithFieldComparator {

                addGroup {

                    predicate { it.toUseSoon }
                    addField { it.useByDate?.date }
                }

                addGroup {

                    predicate { it.hasUseByDate() }
                    addField { it.useByDate?.date }
                }

                addField(nullsFirst = false) { it.category?.name }
                addField { it.name }
            }
        }
    }
}