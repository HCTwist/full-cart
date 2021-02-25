package uk.henrytwist.fullcart.view.item.editpantryitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.models.PantryItemDetails
import uk.henrytwist.fullcart.usecases.*
import uk.henrytwist.fullcart.view.ListSelectionDialogHelper
import uk.henrytwist.fullcart.view.MonitorLiveData
import uk.henrytwist.fullcart.view.TransferButtonDisplay
import uk.henrytwist.fullcart.view.pantryitem.PantryItemViewModel
import javax.inject.Inject

@HiltViewModel
class EditPantryItemViewModel @Inject constructor(
        getAllCategories: GetAllCategories,
        searchItems: SearchItems,
        private val getPantryItem: GetPantryItem,
        private val getListMetaSummaries: GetListMetaSummaries,
        private val editPantryItem: EditPantryItem,
        private val removePantryItem: RemovePantryItem,
        private val addShoppingItem: AddShoppingItem
) : PantryItemViewModel(false, false, getAllCategories, searchItems) {

    private var id = -1
    private lateinit var itemDetails: PantryItemDetails

    val pantrySelectionDialogHelper = ListSelectionDialogHelper(this::onPantryToMoveToSelected, R.string.move_to_dialog_title)
    val shoppingListSelectionDialogHelper = ListSelectionDialogHelper(this::onShoppingListToAddToSelected, R.string.add_to_list_dialog_title)

    private val _pantryMoveButtonDisplay = MutableLiveData<TransferButtonDisplay>()
    val pantryMoveButtonDisplay: LiveData<TransferButtonDisplay>
        get() = _pantryMoveButtonDisplay

    private val _shoppingListAddButtonDisplay = MutableLiveData<TransferButtonDisplay>()
    val shoppingListAddButtonDisplay: LiveData<TransferButtonDisplay>
        get() = _shoppingListAddButtonDisplay

    private val _changeMonitor = MonitorLiveData()
    val hasChanges: LiveData<Boolean>
        get() = _changeMonitor

    fun args(args: EditPantryItemFragmentArgs) {

        id = args.id

        viewModelScope.launch {

            itemDetails = getPantryItem(id)

            name.value = itemDetails.name
            category.value = itemDetails.category
            setQuantity(itemDetails.quantity)
            setUseByDate(itemDetails.useByDate)

            _changeMonitor.monitor(name, category, quantityNumberIndex, quantityUnitIndex, useByDate)

            val lists = getListMetaSummaries()
            val pantries = lists.filter { it.type == ListType.PANTRY && it.id != itemDetails.listId }
            val shoppingLists = lists.filter { it.type == ListType.SHOPPING_LIST && it.id != itemDetails.listId }

            pantrySelectionDialogHelper.setLists(pantries)
            shoppingListSelectionDialogHelper.setLists(shoppingLists)

            _pantryMoveButtonDisplay.value = TransferButtonDisplay(
                    pantrySelectionDialogHelper.isNotEmpty(),
                    R.string.edit_pantry_item_move_pantry,
                    R.string.edit_item_move_single,
                    pantrySelectionDialogHelper.getSingleNameIfExists()
            )
            _shoppingListAddButtonDisplay.value = TransferButtonDisplay(
                    shoppingListSelectionDialogHelper.isNotEmpty(),
                    R.string.edit_pantry_item_add_list,
                    R.string.edit_pantry_item_add_list_single,
                    shoppingListSelectionDialogHelper.getSingleNameIfExists()
            )
        }
    }

    override fun onConfirmClicked() {

        edit(itemDetails.listId)
    }

    fun onDeleteClicked() {

        viewModelScope.launch {

            removePantryItem(itemDetails)
            navigateBack()
        }
    }

    fun onMoveToPantryClicked() {

        pantrySelectionDialogHelper.onSelectListRequest()
    }

    fun onAddToListClicked() {

        shoppingListSelectionDialogHelper.onSelectListRequest()
    }

    private fun onPantryToMoveToSelected(listId: Int) {

        viewModelScope.launch {

            edit(listId)
            navigateBack()
        }
    }

    private fun onShoppingListToAddToSelected(listId: Int) {

        viewModelScope.launch {

            addShoppingItem(listId, name.value!!, category.value, getQuantity())
        }
    }

    private fun edit(listId: Int) {

        viewModelScope.launch {

            editPantryItem(id, listId, name.value!!, category.value, getQuantity(), useByDate.value)
            navigateBack()
        }
    }
}