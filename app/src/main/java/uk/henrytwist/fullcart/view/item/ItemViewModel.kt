package uk.henrytwist.fullcart.view.item

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.fullcart.models.Category
import uk.henrytwist.fullcart.models.Quantity
import uk.henrytwist.fullcart.models.SearchItem
import uk.henrytwist.fullcart.usecases.GetAllCategories
import uk.henrytwist.fullcart.usecases.SearchItems
import uk.henrytwist.fullcart.view.components.categories.CategoryButton

abstract class ItemViewModel(
        private val searchEnabled: Boolean,
        private val autoSetCategory: Boolean,
        private val getAllCategories: GetAllCategories,
        private val searchItems: SearchItems
) : NavigatorViewModel(), CategoryButton.OnCategorySelectedListener, SearchItemAdapter.Handler {

    val name = MutableLiveData("")
    val category = MutableLiveData<Category?>()

    val valid = name.map { it.isNotBlank() }

    private val _showSearch = MutableLiveData(searchEnabled)
    val showSearch: LiveData<Boolean>
        get() = _showSearch

    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>>
        get() = _allCategories

    private val _searchResults = MutableLiveData(listOf<SearchItem>())
    val searchResults: LiveData<List<SearchItem>>
        get() = _searchResults

    private var searchResultsJob: Job? = null

    private val _quantityNumberGenerator = MutableLiveData(Quantity.Unit.SINGLE.getNumberGenerator())
    val quantityNumberGenerator = _quantityNumberGenerator.distinctUntilChanged()

    private val _quantityNumberIndex = MutableLiveData(Quantity.Unit.SINGLE.ordinal)
    val quantityNumberIndex = _quantityNumberIndex.distinctUntilChanged()

    private val _quantityUnitIndex = MutableLiveData(0)
    val quantityUnitIndex = _quantityUnitIndex.distinctUntilChanged()

    init {

        viewModelScope.launch {

            val categories = getAllCategories().first()
            if (autoSetCategory) category.value = categories.find { it.isDefault }
            _allCategories.value = categories
        }

        if (searchEnabled) {

            viewModelScope.launch {

                search("")
            }
        }
    }

    fun onBackClicked() {

        navigateBack()
    }

    fun onNameChanged(n: String) {

        if (name.value == n) {

            return
        }

        name.value = n

        if (searchEnabled) {

            search(n)
        }
    }

    private fun search(query: String) {

        searchResultsJob?.cancel()
        searchResultsJob = viewModelScope.launch {

            val results = searchItems(query)
            _searchResults.value = results
            _showSearch.value = results.isNotEmpty()
        }
    }

    override fun onSearchItemClicked(item: SearchItem) {

        _showSearch.value = false

        name.value = item.name
        category.value = item.category
    }

    override fun onCategorySelected(newCategory: Category?) {

        category.value = newCategory
    }

    fun onQuantityChanged(index: Int) {

        _quantityNumberIndex.value = index
    }

    fun onQuantityUnitChanged(index: Int) {

        _quantityUnitIndex.value = index
        _quantityNumberGenerator.value = Quantity.Unit.values()[index].getNumberGenerator()
    }

    protected fun getQuantity(): Quantity {

        val n = quantityNumberIndex.value!!
        val u = Quantity.Unit.values()[quantityUnitIndex.value!!]
        return Quantity(quantityNumberGenerator.value!!.generate(n), u)
    }

    protected fun setQuantity(q: Quantity) {

        val generator = q.unit.getNumberGenerator()
        _quantityNumberGenerator.value = generator

        var i = 0
        while (i < Int.MAX_VALUE) {

            val n = generator.generate(i)
            if (n == q.number) {

                _quantityNumberIndex.value = i
                break
            }

            i++
        }

        _quantityUnitIndex.value = q.unit.ordinal
    }

    abstract fun onConfirmClicked()
}