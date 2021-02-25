package uk.henrytwist.fullcart.view.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.SearchItemRowBinding
import uk.henrytwist.fullcart.models.SearchItem

class SearchItemAdapter : RecyclerView.Adapter<SearchItemAdapter.Holder>() {

    var searchItems = listOf<SearchItem>()
    lateinit var handler: Handler

    override fun getItemCount(): Int {

        return searchItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(SearchItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(searchItems[position], handler)
    }

    class Holder(private val binding: SearchItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchItem: SearchItem, handler: Handler) {

            binding.item = searchItem
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onSearchItemClicked(item: SearchItem)
    }
}