package uk.henrytwist.fullcart.view.item

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import uk.henrytwist.fullcart.databinding.ItemBinding
import uk.henrytwist.fullcart.view.components.quantity.QuantityNumberAdapter
import uk.henrytwist.fullcart.view.components.quantity.QuantityUnitAdapter
import uk.henrytwist.selectslider.SelectSliderView

abstract class ItemFragment : Fragment() {

    abstract val itemViewModel: ItemViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        itemViewModel.observeNavigation(this)

        val binding = getItemBinding()

        binding.editItemNameContainer.addTextChangedListener {

            if (it != null) itemViewModel.onNameChanged(it.toString())
        }

        itemViewModel.allCategories.observe(viewLifecycleOwner) {

            binding.editItemCategory.categories = it
        }

        binding.editItemCategory.onCategorySelectedListener = itemViewModel

        val quantityAdapter = QuantityNumberAdapter()
        itemViewModel.quantityNumberGenerator.observe(viewLifecycleOwner) {

            quantityAdapter.numberGenerator = it
            quantityAdapter.notifyDataSetChanged()
        }
        binding.editItemQuantity.onSelectListener = SelectSliderView.OnSelectListener {

            itemViewModel.onQuantityChanged(it)
        }
        binding.editItemQuantity.run {

            setHasFixedSize(true)
            adapter = quantityAdapter
        }
        itemViewModel.quantityNumberIndex.observe(viewLifecycleOwner) {

            binding.editItemQuantity.setSelection(it)
        }

        val quantityUnitAdapter = QuantityUnitAdapter()
        binding.editItemQuantityUnit.onSelectListener = SelectSliderView.OnSelectListener {

            itemViewModel.onQuantityUnitChanged(it)
        }
        binding.editItemQuantityUnit.run {

            setHasFixedSize(true)
            adapter = quantityUnitAdapter
        }
        itemViewModel.quantityUnitIndex.observe(viewLifecycleOwner) {

            binding.editItemQuantityUnit.setSelection(it)
        }
    }

    abstract fun getItemBinding(): ItemBinding
}