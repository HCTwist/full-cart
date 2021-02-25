package uk.henrytwist.fullcart.view.categories

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.view.components.HeaderAdapter
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
        getAllCategories: GetAllCategories
) : NavigatorViewModel(), CategoriesAdapter.Handler, HeaderAdapter.Handler {

    val categories = getAllCategories().asLiveData()

    val empty = categories.map { it.isEmpty() }

    fun onClickAdd() {

        navigate(R.id.action_categoriesFragment_to_addCategoryFragment)
    }

    override fun onCategoryClicked(category: Category) {

    }

    override fun onClickBack() {

        navigateBack()
    }
}