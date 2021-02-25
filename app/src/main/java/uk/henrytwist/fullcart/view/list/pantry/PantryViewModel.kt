package uk.henrytwist.fullcart.view.list.pantry

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.models.PantryItemSummary
import uk.henrytwist.fullcart.usecases.*
import uk.henrytwist.fullcart.view.list.ListContainerFragmentDirections
import uk.henrytwist.fullcart.view.list.ListViewModel
import javax.inject.Inject

@HiltViewModel
class PantryViewModel @Inject constructor(
        onboardingRepository: OnboardingRepository,
        getListMeta: GetListMeta,
        private val setCurrentList: SetCurrentList,
        private val getPantryItemSummaries: GetPantryItemSummaries,
        private val decrementPantryItem: DecrementPantryItem,
        private val deletePantry: DeletePantry
) : ListViewModel(onboardingRepository, getListMeta), PantryAdapter.Handler {

    private val cancelOnDeleteCoroutineContext = Job()
    lateinit var items: LiveData<List<PantryItemSummary>>

    override fun init(listId: Int) {

        items = getPantryItemSummaries(listId).map {

            emptyItems.value = it.isEmpty()
            loadingItems.value = false
            it
        }.asLiveData(cancelOnDeleteCoroutineContext)

        viewModelScope.launch {

            setCurrentList(listId)
        }
    }

    fun onClickAdd() {

        navigate(ListContainerFragmentDirections.actionListContainerFragmentToAddPantryItemFragment(listId))
    }

    fun onClickDeleteList() {

        cancelOnDeleteCoroutineContext.cancel()
        viewModelScope.launch {

            deletePantry(listId)
            navigate(R.id.action_listContainerFragment_self)
        }
    }

    override fun onPantryItemClicked(item: PantryItemSummary) {

        navigate(ListContainerFragmentDirections.actionListContainerFragmentToEditPantryItemFragment(item.id))
    }

    override fun onPantryItemDecrementClicked(item: PantryItemSummary) {

        viewModelScope.launch {

            decrementPantryItem(item)
        }
    }
}