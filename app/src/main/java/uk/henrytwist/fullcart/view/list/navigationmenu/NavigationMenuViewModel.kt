package uk.henrytwist.fullcart.view.list.navigationmenu

import android.view.Menu
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.ListMetaSummary
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.usecases.GetCurrentList
import uk.henrytwist.fullcart.usecases.GetListMetaSummaries
import uk.henrytwist.fullcart.view.IdBundle
import javax.inject.Inject

@HiltViewModel
class NavigationMenuViewModel @Inject constructor(
        private val getCurrentList: GetCurrentList,
        private val getListMetaSummaries: GetListMetaSummaries
) : NavigatorViewModel() {

    private val summaryMenuItems = mutableMapOf<Int, ListMetaSummary>()
    private var currentListId: Int? = null

    fun init(menu: Menu) {

        viewModelScope.launch {

            val summaries = getListMetaSummaries()
            currentListId = getCurrentList()

            summaries.forEach {

                val itemId = View.generateViewId()
                val item = menu.add(R.id.list_menu_lists, itemId, 0, it.name)
                item.setIcon(if (it.type == ListType.SHOPPING_LIST) R.drawable.outline_list_alt_24 else R.drawable.outline_kitchen_24)
                item.isCheckable = true
                item.isChecked = it.id == currentListId

                summaryMenuItems[itemId] = it
            }
        }
    }

    fun onItemSelected(id: Int) {

        when (id) {

            R.id.list_menu_add_list -> navigate(R.id.action_listContainerFragment_to_addListFragment)
            R.id.list_menu_categories -> navigate(R.id.action_listContainerFragment_to_categoriesFragment)
            R.id.list_menu_settings -> navigate(R.id.action_listContainerFragment_to_settingsFragment)
            else -> {

                val summary = summaryMenuItems[id]
                if (summary != null && summary.id != currentListId) {

                    navigate(R.id.action_listContainerFragment_self, IdBundle.pack(summary.id))
                }
            }
        }
    }
}