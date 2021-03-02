package uk.henrytwist.fullcart.view.category.editcategory

import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.CategoryColor
import uk.henrytwist.fullcart.usecases.EditCategory
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.usecases.GetCategory
import uk.henrytwist.fullcart.usecases.RemoveCategory
import uk.henrytwist.fullcart.view.MonitorLiveData
import uk.henrytwist.fullcart.view.category.CategoryViewModel
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
        getAllCategories: GetAllCategories,
        private val getCategory: GetCategory,
        private val editCategory: EditCategory,
        private val removeCategory: RemoveCategory
) : CategoryViewModel(getAllCategories) {

    private var id = -1

    val hasChanges = MonitorLiveData()

    fun args(args: EditCategoryFragmentArgs) {

        id = args.id
        viewModelScope.launch {

            val category = getCategory(id)

            name.value = category.name
            CategoryColor.values().find { it.colorHex == category.color }?.let {

                mutableSelectedColor.value = it
            }
            isDefault.value = category.isDefault

            hasChanges.monitor(name.distinctUntilChanged(), selectedColor, isDefault.distinctUntilChanged())
        }
    }

    fun onSaveClicked() {

        viewModelScope.launch {

            editCategory(Category(id, name.value!!, selectedColor.value!!.colorHex, isDefault.value!!))
            navigateBack()
        }
    }

    fun onDeleteClicked() {

        viewModelScope.launch {

            removeCategory(id)
            navigateBack()
        }
    }
}