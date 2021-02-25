package uk.henrytwist.fullcart.view.item.addpantryitem

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.models.NewPantryItem
import uk.henrytwist.fullcart.usecases.AddPantryItem
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.usecases.SearchItems
import uk.henrytwist.fullcart.view.pantryitem.PantryItemViewModel
import javax.inject.Inject

@HiltViewModel
class AddPantryItemViewModel @Inject constructor(
        private val addPantryItem: AddPantryItem,
        getAllCategories: GetAllCategories,
        searchItems: SearchItems
) : PantryItemViewModel(true, true, getAllCategories, searchItems) {

    private var listId = -1

    fun args(addPantryItemArgs: AddPantryItemFragmentArgs) {

        listId = addPantryItemArgs.listId
    }

    override fun onConfirmClicked() {

        viewModelScope.launch {

            addPantryItem(NewPantryItem(
                    listId,
                    name.value!!,
                    category.value,
                    getQuantity(),
                    useByDate.value
            ))
            navigateBack()
        }
    }
}