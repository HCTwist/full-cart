package uk.henrytwist.fullcart.view.categories

import androidx.recyclerview.widget.DiffUtil
import uk.henrytwist.fullcart.models.Category

object CategoryDiff : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {

        return oldItem.name == newItem.name &&
                oldItem.color == newItem.color &&
                oldItem.isDefault == newItem.isDefault
    }
}