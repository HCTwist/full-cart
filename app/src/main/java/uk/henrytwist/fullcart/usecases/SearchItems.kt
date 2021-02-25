package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.data.searchitems.SearchItemModel
import uk.henrytwist.fullcart.data.searchitems.SearchItemRepository
import uk.henrytwist.fullcart.models.SearchItem
import java.time.Instant
import javax.inject.Inject
import kotlin.math.pow

class SearchItems @Inject constructor(private val searchItemRepository: SearchItemRepository, private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(query: String): List<SearchItem> {

        val maxCount = 3

        if (query.isBlank()) {

            val mostRecent = searchItemRepository.getMostRecent(maxCount)
            return mostRecent.map {

                SearchItem(it.name, getCategory(it.category))
            }
        } else {

            val searchResults = searchItemRepository.search(query)
            val rankedSearchResults = rank(searchResults)
            return rankedSearchResults.take(maxCount).map {

                SearchItem(it.name, getCategory(it.category))
            }
        }
    }

    private fun rank(items: List<SearchItemModel>): List<SearchItemModel> {

        val dayDecay = 0.9F

        val nowEpochSecond = Instant.now().epochSecond
        return items.sortedBy {

            val dtDays = (nowEpochSecond.toFloat() - it.lastUpdatedEpochSecond) / 86400
            it.count * dtDays.pow(dayDecay)
        }
    }

    private suspend fun getCategory(category: Int?) = if (category == null) {

        null
    } else {

        categoryRepository.get(category)
    }
}