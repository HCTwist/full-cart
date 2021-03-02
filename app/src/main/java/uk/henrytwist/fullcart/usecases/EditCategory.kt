package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.models.Category
import javax.inject.Inject

class EditCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(category: Category) {

        categoryRepository.edit(category)
    }
}