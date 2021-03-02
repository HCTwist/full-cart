package uk.henrytwist.fullcart.view.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.models.CategoryColor
import uk.henrytwist.fullcart.usecases.GetAllCategories

abstract class CategoryViewModel(
        private val getAllCategories: GetAllCategories,
) : NavigatorViewModel() {

    val name = MutableLiveData("")

    protected val mutableSelectedColor = MutableLiveData(CategoryColor.values()[0])
    val selectedColor: LiveData<CategoryColor>
        get() = mutableSelectedColor

    val isDefault = MutableLiveData(false)

    private var categoryNames: List<String>? = null
    val valid = name.map { categoryNames != null && it.isNotBlank() && !categoryNames!!.contains(it) }

    init {

        viewModelScope.launch {

            categoryNames = getAllCategories().first().map { it.name }
        }
    }

    fun onColorSelected(position: Int) {

        val actualPosition = position % CategoryColor.values().size
        mutableSelectedColor.value = CategoryColor.values()[actualPosition]
    }
}