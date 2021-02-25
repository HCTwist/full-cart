package uk.henrytwist.fullcart.view.item

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.models.SearchItem
import uk.henrytwist.fullcart.view.hideSoftKeyboard

abstract class SearchableItemFragment : ItemFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val searchAdapter = SearchItemAdapter()
        searchAdapter.handler = object : SearchItemAdapter.Handler {

            override fun onSearchItemClicked(item: SearchItem) {

                val nameView = getEditItemBinding().editItemNameContainer
                nameView.hideSoftKeyboard()
                nameView.clearFocus()
                itemViewModel.onSearchItemClicked(item)
            }
        }

        getSearchRecycler().run {

            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        itemViewModel.searchResults.observe(viewLifecycleOwner) {

            searchAdapter.searchItems = it
            searchAdapter.notifyDataSetChanged()
        }
    }

    abstract fun getSearchRecycler(): RecyclerView
}