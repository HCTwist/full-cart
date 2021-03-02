package uk.henrytwist.fullcart.view.item.editshoppingitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.models.ShoppingItemDetails
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import uk.henrytwist.fullcart.usecases.*
import uk.henrytwist.fullcart.view.ListSelectionDialogHelper
import uk.henrytwist.fullcart.view.MonitorLiveData
import uk.henrytwist.fullcart.view.TransferButtonDisplay
import uk.henrytwist.fullcart.view.item.ItemViewModel
import javax.inject.Inject

@HiltViewModel
class EditShoppingItemViewModel @Inject constructor(
        getAllCategories: GetAllCategories,
        searchItems: SearchItems,
        private val getShoppingItem: GetShoppingItem,
        private val getListMetaSummaries: GetListMetaSummaries,
        private val editShoppingItem: EditShoppingItem,
        private val removeShoppingItems: RemoveShoppingItems,
        private val moveFromListToPantry: MoveFromListToPantry,
        private val moveFromListToList: MoveFromListToList
) : ItemViewModel(false, false, getAllCategories, searchItems) {

    private var id = -1
    private lateinit var itemDetails: ShoppingItemDetails

    private val _checked = MutableLiveData<Boolean>()
    val checked = _checked.immutable()

    val pantrySelectionDialogHelper = ListSelectionDialogHelper(this::onPantryToMoveToSelected, R.string.move_to_dialog_title)
    val shoppingListSelectionDialogHelper = ListSelectionDialogHelper(this::onShoppingListToMoveToSelected, R.string.move_to_dialog_title)

    private val _pantryMoveButtonDisplay = MutableLiveData<TransferButtonDisplay>()
    val pantryMoveButtonDisplay: LiveData<TransferButtonDisplay>
        get() = _pantryMoveButtonDisplay

    private val _shoppingListMoveButtonDisplay = MutableLiveData<TransferButtonDisplay>()
    val shoppingListMoveButtonDisplay: LiveData<TransferButtonDisplay>
        get() = _shoppingListMoveButtonDisplay

    private val _changeMonitor = MonitorLiveData()
    val hasChanges: LiveData<Boolean>
        get() = _changeMonitor

    fun args(args: EditShoppingItemFragmentArgs) {

        id = args.id

        viewModelScope.launch {

            itemDetails = getShoppingItem(id)

            name.value = itemDetails.name
            category.value = itemDetails.category
            setQuantity(itemDetails.quantity)
            _checked.value = itemDetails.checked

            _changeMonitor.monitor(name, category, quantityNumberIndex, quantityUnitIndex)

            val lists = getListMetaSummaries()
            val pantries = lists.filter { it.type == ListType.PANTRY && it.id != itemDetails.listId }
            val shoppingLists = lists.filter { it.type == ListType.SHOPPING_LIST && it.id != itemDetails.listId }

            pantrySelectionDialogHelper.setLists(pantries)
            shoppingListSelectionDialogHelper.setLists(shoppingLists)

            _pantryMoveButtonDisplay.value = TransferButtonDisplay(
                    pantrySelectionDialogHelper.isNotEmpty(),
                    R.string.edit_shopping_item_move_pantry,
                    R.string.edit_item_move_single,
                    pantrySelectionDialogHelper.getSingleNameIfExists()
            )
            _shoppingListMoveButtonDisplay.value = TransferButtonDisplay(
                    shoppingListSelectionDialogHelper.isNotEmpty(),
                    R.string.edit_shopping_item_move_list,
                    R.string.edit_item_move_single,
                    shoppingListSelectionDialogHelper.getSingleNameIfExists()
            )
        }
    }

    override fun onConfirmClicked() {

        edit(itemDetails.listId)
    }

    fun onDeleteClicked() {

        viewModelScope.launch {

            removeShoppingItems(listOf(itemDetails))
            navigateBack()
        }
    }

    fun onMoveToPantryClicked() {

        pantrySelectionDialogHelper.onSelectListRequest()
    }

    fun onMoveToListClicked() {

        shoppingListSelectionDialogHelper.onSelectListRequest()
    }

    private fun onPantryToMoveToSelected(listId: Int) {

        viewModelScope.launch {

            moveFromListToPantry(listOf(ShoppingItemSummary(id, name.value!!, category.value, getQuantity(), itemDetails.checked)), listId)
            navigateBack()
        }
    }

    private fun onShoppingListToMoveToSelected(listId: Int) {

        viewModelScope.launch {

            moveFromListToList(itemDetails, listId)
            navigateBack()
        }
    }

    private fun edit(listId: Int) {

        viewModelScope.launch {

            val editedItem = ShoppingItemDetails(id, listId, name.value!!, category.value, getQuantity(), itemDetails.checked)
            editShoppingItem(editedItem)
            navigateBack()
        }
    }
}