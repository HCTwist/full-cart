package uk.henrytwist.fullcart.usecases

import kotlinx.coroutines.flow.Flow
import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.models.Category
import javax.inject.Inject

class GetAllCategories @Inject constructor(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): Flow<List<Category>> {

        return categoryRepository.getAll()
    }
}