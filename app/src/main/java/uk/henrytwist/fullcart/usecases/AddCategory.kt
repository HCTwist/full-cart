package uk.henrytwist.fullcart.usecases

import uk.henrytwist.fullcart.data.categories.CategoryRepository
import uk.henrytwist.fullcart.models.NewCategory
import javax.inject.Inject

class AddCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(newCategory: NewCategory) {

        categoryRepository.add(newCategory)
    }
}