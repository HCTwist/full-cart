package uk.henrytwist.fullcart.view.list

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import uk.henrytwist.kotlinbasics.Trigger
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.data.OnboardingRepository
import uk.henrytwist.fullcart.models.ListMeta
import uk.henrytwist.fullcart.usecases.GetListMeta
import uk.henrytwist.fullcart.view.IdBundle

abstract class ListViewModel(
        private val onboardingRepository: OnboardingRepository,
        private val getListMeta: GetListMeta
) : NavigatorViewModel(), ListHeaderAdapter.Handler {

    protected var listId = -1
    protected lateinit var listMeta: ListMeta

    lateinit var title: LiveData<String>

    private val _showHeaderTouchPrompt = MutableLiveData(!onboardingRepository.hasExploredMenu())
    val showHeaderTouchPrompt: LiveData<Boolean>
        get() = _showHeaderTouchPrompt

    private val _showNavigationMenu = MutableLiveData<Trigger>()
    val showNavigationMenu: LiveData<Trigger>
        get() = _showNavigationMenu

    private val _showMenu = MutableLiveData<Trigger>()
    val showMenu: LiveData<Trigger>
        get() = _showMenu

    protected val emptyItems = MutableLiveData(false)
    val empty: LiveData<Boolean>
        get() = emptyItems

    protected val loadingItems = MutableLiveData(true)
    val loading: LiveData<Boolean>
        get() = loadingItems

    fun args(bundle: Bundle) {

        this.listId = IdBundle.unpack(bundle)

        title = liveData {

            val meta = getListMeta(listId)

            if (meta == null) {

                navigate(R.id.action_listContainerFragment_self)
            } else {

                listMeta = meta
                emit(listMeta.name)
            }
        }

        init(listId)
    }

    protected abstract fun init(listId: Int)

    override fun onTitleClick() {

        onboardingRepository.setHasExploredMenu()
        _showHeaderTouchPrompt.value = false
        _showNavigationMenu.trigger()
    }

    override fun onMenuClick() {

        _showMenu.trigger()
    }

    override fun onCleared() {
        super.onCleared()
    }
}