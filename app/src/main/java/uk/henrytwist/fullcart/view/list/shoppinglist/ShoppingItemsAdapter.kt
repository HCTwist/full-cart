package uk.henrytwist.fullcart.view.list.shoppinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.ShoppingItemRowBinding
import uk.henrytwist.fullcart.databinding.ShoppingListBreakBinding
import uk.henrytwist.fullcart.models.ShoppingItemSummary

class ShoppingItemsAdapter : ListAdapter<ShoppingListRow, RecyclerView.ViewHolder>(ShoppingListRowDiff) {

    lateinit var handler: Handler

    override fun getItemCount(): Int {

        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (currentList[position]) {

            is ShoppingListRow.Item -> VIEW_TYPE_ITEM
            is ShoppingListRow.Divider -> VIEW_TYPE_DIVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            VIEW_TYPE_ITEM -> ItemHolder(ShoppingItemRowBinding.inflate(inflater, parent, false))
            VIEW_TYPE_DIVIDER -> BreakHolder(ShoppingListBreakBinding.inflate(inflater, parent, false))
            else -> throw RuntimeException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val row = currentList[position]

        when {

            holder is ItemHolder && row is ShoppingListRow.Item -> holder.bind(row.item, handler)
            holder is BreakHolder && row is ShoppingListRow.Divider -> holder.bind(row, handler)
            else -> throw RuntimeException("Unknown item and holder combination")
        }
    }

    class ItemHolder(private val binding: ShoppingItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItemSummary, handler: Handler) {

            binding.item = item
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    class BreakHolder(private val binding: ShoppingListBreakBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(divider: ShoppingListRow.Divider, handler: Handler) {

            binding.divider = divider
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onItemClicked(item: ShoppingItemSummary)

        fun onItemChecked(item: ShoppingItemSummary)

        fun onMoveCheckedToPantryClicked()

        fun onDeleteCheckedClicked()
    }

    companion object {

        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_DIVIDER = 2
    }
}