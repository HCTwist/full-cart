package uk.henrytwist.fullcart.view.list.nolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.henrytwist.androidbasics.Trigger
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R

class NoListViewModel : NavigatorViewModel() {

    private val _showNavigationMenu = MutableLiveData<Trigger>()
    val showNavigationMenu: LiveData<Trigger>
        get() = _showNavigationMenu

    fun onClickMenu() {

        _showNavigationMenu.trigger()
    }

    fun onClickAddList() {

        navigate(R.id.action_listContainerFragment_to_addListFragment)
    }
}