package uk.henrytwist.fullcart.view.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.CategoryRowBinding
import uk.henrytwist.fullcart.models.Category

class CategoriesAdapter : ListAdapter<Category, CategoriesAdapter.Holder>(CategoryDiff) {

    lateinit var handler: Handler

    override fun getItemCount(): Int {

        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(CategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(currentList[position], handler)
    }

    class Holder(private val binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, handler: Handler) {

            binding.category = category
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onCategoryClicked(category: Category)
    }
}