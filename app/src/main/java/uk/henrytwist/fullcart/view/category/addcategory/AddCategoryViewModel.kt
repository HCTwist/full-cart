package uk.henrytwist.fullcart.view.category.addcategory

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.models.NewCategory
import uk.henrytwist.fullcart.usecases.AddCategory
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.view.category.CategoryViewModel
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
        getAllCategories: GetAllCategories,
        private val addCategory: AddCategory
) : CategoryViewModel(getAllCategories) {

    fun onClickAdd() {

        viewModelScope.launch {

            addCategory(NewCategory(name.value!!, selectedColor.value!!.colorHex, isDefault.value!!))
            navigateBack()
        }
    }
}