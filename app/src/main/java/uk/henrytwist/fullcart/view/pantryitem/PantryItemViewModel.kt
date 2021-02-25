package uk.henrytwist.fullcart.view.pantryitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.henrytwist.androidbasics.Trigger
import uk.henrytwist.androidbasics.livedata.trigger
import uk.henrytwist.fullcart.models.UseByDate
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.usecases.SearchItems
import uk.henrytwist.fullcart.view.item.ItemViewModel

abstract class PantryItemViewModel(
        searchEnabled: Boolean,
        autoSetCategory: Boolean,
        getAllCategories: GetAllCategories,
        searchItems: SearchItems,
) : ItemViewModel(searchEnabled, autoSetCategory, getAllCategories, searchItems) {

    private val _useByDate = MutableLiveData<UseByDate?>()
    val useByDate: LiveData<UseByDate?>
        get() = _useByDate

    private val _pickUseByDate = MutableLiveData<Trigger>()
    val pickUseByDate: LiveData<Trigger>
        get() = _pickUseByDate

    fun onUseByDateClicked() {

        _pickUseByDate.trigger()
    }

    fun onUseByDateCleared() {

        _useByDate.value = null
    }

    fun onUseByDatePicked(epochMillis: Long) {

        _useByDate.value = UseByDate.fromEpochMillis(epochMillis)
    }

    protected fun setUseByDate(useByDate: UseByDate?) {

        _useByDate.value = useByDate
    }
}