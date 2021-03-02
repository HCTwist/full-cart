package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.models.Category
import javax.inject.Inject

class GetCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(id: Int): Category {

        return categoryRepository.get(id)
    }
}