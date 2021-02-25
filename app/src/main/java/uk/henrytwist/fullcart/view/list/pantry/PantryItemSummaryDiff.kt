package uk.henrytwist.fullcart.view.list.pantry

import androidx.recyclerview.widget.DiffUtil
import uk.henrytwist.fullcart.models.PantryItemSummary

object PantryItemSummaryDiff : DiffUtil.ItemCallback<PantryItemSummary>() {

    override fun areItemsTheSame(oldItem: PantryItemSummary, newItem: PantryItemSummary): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PantryItemSummary, newItem: PantryItemSummary): Boolean {

        return oldItem.name == newItem.name &&
                oldItem.category?.color == newItem.category?.color &&
                oldItem.quantity == newItem.quantity &&
                oldItem.useByDate == newItem.useByDate &&
                oldItem.toUseSoon == newItem.toUseSoon

    }
}