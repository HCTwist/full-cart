package uk.henrytwist.fullcart.view.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uk.henrytwist.fullcart.databinding.CategoryBinding
import uk.henrytwist.selectslider.SelectSliderView

abstract class CategoryFragment : Fragment() {

    abstract val categoryViewModel: CategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        categoryViewModel.observeNavigation(this)

        val colorAdapter = CategoryColorAdapter()
        getCategoryBinding().addCategoryColors.run {

            setHasFixedSize(true)
            adapter = colorAdapter

            onSelectListener = SelectSliderView.OnSelectListener {

                categoryViewModel.onColorSelected(it)
            }
        }
    }

    abstract fun getCategoryBinding(): CategoryBinding
}