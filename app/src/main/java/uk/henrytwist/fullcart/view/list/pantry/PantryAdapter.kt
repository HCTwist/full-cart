package uk.henrytwist.fullcart.view.list.pantry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.PantryItemRowBinding
import uk.henrytwist.fullcart.models.PantryItemSummary

class PantryAdapter : ListAdapter<PantryItemSummary, PantryAdapter.Holder>(PantryItemSummaryDiff) {

    lateinit var handler: Handler

    override fun getItemCount(): Int {

        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(PantryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(currentList[position], handler)
    }

    class Holder(val binding: PantryItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PantryItemSummary, handler: Handler) {

            binding.item = item
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onPantryItemClicked(item: PantryItemSummary)

        fun onPantryItemDecrementClicked(item: PantryItemSummary)
    }
}