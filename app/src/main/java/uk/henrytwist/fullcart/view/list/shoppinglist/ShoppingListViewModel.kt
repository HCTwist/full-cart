package uk.henrytwist.fullcart.view.list.shoppinglist

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.models.ShoppingItemSummary
import uk.henrytwist.fullcart.usecases.*
import uk.henrytwist.fullcart.view.ListSelectionDialogHelper
import uk.henrytwist.fullcart.view.list.ListContainerFragmentDirections
import uk.henrytwist.fullcart.view.list.ListViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
        onboardingRepository: OnboardingRepository,
        getListMeta: GetListMeta,
        private val setCurrentList: SetCurrentList,
        private val getShoppingItemSummaries: GetShoppingItemSummaries,
        private val toggleShoppingItemCheck: ToggleShoppingItemCheck,
        private val moveFromListToPantry: MoveFromListToPantry,
        private val removeShoppingItems: RemoveShoppingItems,
        private val deleteShoppingList: DeleteShoppingList,
        private val getPantrySummaries: GetPantrySummaries
) : ListViewModel(onboardingRepository, getListMeta), ShoppingItemsAdapter.Handler, ListSelectionDialogHelper.Handler {

    private val cancelOnDeleteCoroutineContext = Job()
    private val _rows = MediatorLiveData<List<ShoppingListRow>>()
    val rows: LiveData<List<ShoppingListRow>>
        get() = _rows

    val pantrySelectionDialogHelper = ListSelectionDialogHelper(this, R.string.move_to_dialog_title)

    override fun init(listId: Int) {

        viewModelScope.launch {

            pantrySelectionDialogHelper.setLists(getPantrySummaries())

            val rowsData = getShoppingItemSummaries(listId).cancellable().map { list ->

                emptyItems.value = list.isEmpty()

                val r = ArrayList<ShoppingListRow>(list.size + 1)

                list.forEach {

                    if (!it.checked) {

                        r.add(ShoppingListRow.Item(it))
                    }
                }

                var addedBreak = false

                list.forEach {

                    if (it.checked) {

                        if (!addedBreak) {

                            val singlePantryName = pantrySelectionDialogHelper.getSingleNameIfExists()
                            r.add(ShoppingListRow.Divider(pantrySelectionDialogHelper.isNotEmpty(), singlePantryName))
                            addedBreak = true
                        }
                        r.add(ShoppingListRow.Item(it))
                    }
                }

                loadingItems.value = false

                r
            }.asLiveData(cancelOnDeleteCoroutineContext)

            _rows.addSource(rowsData) { _rows.value = it }

            setCurrentList(listId)
        }
    }

    override fun onItemClicked(item: ShoppingItemSummary) {

        navigate(ListContainerFragmentDirections.actionListContainerFragmentToEditShoppingItemFragment(item.id))
    }

    override fun onItemChecked(item: ShoppingItemSummary) {

        viewModelScope.launch {

            toggleShoppingItemCheck(item)
        }
    }

    override fun onMoveCheckedToPantryClicked() {

        pantrySelectionDialogHelper.onSelectListRequest()
    }

    override fun onListSelected(listId: Int) {

        viewModelScope.launch {

            getCheckedItems()?.let { moveFromListToPantry(it, listId) }
        }
    }

    override fun onDeleteCheckedClicked() {

        viewModelScope.launch {

            getCheckedItems()?.let { removeShoppingItems(it) }
        }
    }

    fun onClickAdd() {

        navigate(ListContainerFragmentDirections.actionListContainerFragmentToAddShoppingItemFragment(listId))
    }

    private fun getCheckedItems() = rows.value?.filterIsInstance<ShoppingListRow.Item>()?.filter {

        it.item.checked
    }?.map {

        it.item
    }

    fun onClickDeleteList() {

        cancelOnDeleteCoroutineContext.cancel()
        viewModelScope.launch {

            deleteShoppingList(listId)
            navigate(R.id.action_listContainerFragment_self)
        }
    }
}