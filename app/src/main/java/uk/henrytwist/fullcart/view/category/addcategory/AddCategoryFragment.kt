package uk.henrytwist.fullcart.view.category.addcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.AddCategoryFragmentBinding
import uk.henrytwist.fullcart.databinding.CategoryBinding
import uk.henrytwist.fullcart.view.category.CategoryFragment
import uk.henrytwist.fullcart.view.category.CategoryViewModel
import uk.henrytwist.fullcart.view.showSoftKeyboard

@AndroidEntryPoint
class AddCategoryFragment : CategoryFragment() {

    private lateinit var binding: AddCategoryFragmentBinding

    private val viewModel by viewModels<AddCategoryViewModel>()

    override val categoryViewModel: CategoryViewModel
        get() = viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddCategoryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.category.addCategoryName.showSoftKeyboard()
    }

    override fun getCategoryBinding(): CategoryBinding {

        return binding.category
    }
}