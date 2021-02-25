package uk.henrytwist.fullcart.view.addcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.models.CategoryColor
import uk.henrytwist.fullcart.models.NewCategory
import uk.henrytwist.fullcart.usecases.AddCategory
import uk.henrytwist.fullcart.usecases.GetAllCategories
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
        private val getAllCategories: GetAllCategories,
        private val addCategory: AddCategory
) : NavigatorViewModel() {

    val name = MutableLiveData("")

    private val _selectedColor = MutableLiveData(CategoryColor.values()[0])
    val selectedColor: LiveData<CategoryColor>
        get() = _selectedColor

    var isDefault = false

    private var categoryNames: List<String>? = null
    val valid = name.map { categoryNames != null && it.isNotBlank() && !categoryNames!!.contains(it) }

    init {

        viewModelScope.launch {

            categoryNames = getAllCategories().first().map { it.name }
        }
    }

    fun onColorSelected(position: Int) {

        val actualPosition = position % CategoryColor.values().size
        _selectedColor.value = CategoryColor.values()[actualPosition]
    }

    fun onDefaultCheckChanged(checked: Boolean) {

        isDefault = checked
    }

    fun onClickAdd() {

        viewModelScope.launch {

            addCategory(NewCategory(name.value!!, selectedColor.value!!.colorHex, isDefault))
            navigateBack()
        }
    }
}