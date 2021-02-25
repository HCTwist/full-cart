package uk.henrytwist.fullcart.view.item.addshoppingitem

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.usecases.AddShoppingItem
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.usecases.SearchItems
import uk.henrytwist.fullcart.view.item.ItemViewModel
import javax.inject.Inject

@HiltViewModel
class AddShoppingItemViewModel @Inject constructor(
        getAllCategories: GetAllCategories,
        searchItems: SearchItems,
        private val addShoppingItem: AddShoppingItem
) : ItemViewModel(true, true, getAllCategories, searchItems) {

    private var listId = -1

    fun args(addShoppingItemFragmentArgs: AddShoppingItemFragmentArgs) {

        listId = addShoppingItemFragmentArgs.listId
    }

    override fun onConfirmClicked() {

        viewModelScope.launch {

            addShoppingItem(listId, name.value!!, category.value, getQuantity())
            navigateBack()
        }
    }
}