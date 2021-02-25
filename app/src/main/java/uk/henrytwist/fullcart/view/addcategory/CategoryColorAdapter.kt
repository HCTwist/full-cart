package uk.henrytwist.fullcart.view.addcategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.fullcart.databinding.CategoryColorRowBinding
import uk.henrytwist.fullcart.models.CategoryColor
import uk.henrytwist.selectslider.SelectSliderAdapter

class CategoryColorAdapter : SelectSliderAdapter<CategoryColorAdapter.Holder>() {

    private val colors = CategoryColor.values()

    override fun getItemCount(): Int {

        return Integer.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(CategoryColorRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindSliderViewHolder(holder: Holder, position: Int) {

        holder.bind(colors[position % colors.size])
    }

    override fun onAnimateViewHolder(holder: Holder, selectedFraction: Float) {

        holder.binding.categoryColorView.setSelectionFraction(selectedFraction)
    }

    class Holder(val binding: CategoryColorRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(color: CategoryColor) {

            binding.color = color
            binding.executePendingBindings()
        }
    }
}