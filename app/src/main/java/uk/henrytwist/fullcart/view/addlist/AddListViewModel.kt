package uk.henrytwist.fullcart.view.addlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.models.NewList
import uk.henrytwist.fullcart.usecases.AddList
import uk.henrytwist.fullcart.usecases.GetListMetaSummaries
import uk.henrytwist.fullcart.usecases.SetCurrentList
import javax.inject.Inject

@HiltViewModel
class AddListViewModel @Inject constructor(
        private val getListMetaSummaries: GetListMetaSummaries,
        private val setCurrentList: SetCurrentList,
        private val addList: AddList
) : NavigatorViewModel() {

    val name = MutableLiveData("")
    private var type = ListType.SHOPPING_LIST

    private var currentListNames: List<String>? = null
    val valid = name.map { it.isNotBlank() && !currentListNames!!.contains(it) && currentListNames != null }

    init {

        viewModelScope.launch {

            currentListNames = getListMetaSummaries().map { it.name }
        }
    }

    fun onBackClicked() {

        navigateBack()
    }

    fun onTypeChanged(id: Int) {

        type = when (id) {

            R.id.add_list_type_shopping -> ListType.SHOPPING_LIST
            R.id.add_list_type_pantry -> ListType.PANTRY
            else -> throw IllegalArgumentException("Not a valid type view id")
        }
    }

    fun onClickAdd() {

        viewModelScope.launch {

            val id = addList(NewList(name.value!!, type))
            setCurrentList(id)
            navigate(R.id.action_addListFragment_to_listContainerFragment)
        }
    }
}