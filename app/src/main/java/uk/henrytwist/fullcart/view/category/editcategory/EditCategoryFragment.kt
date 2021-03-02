package uk.henrytwist.fullcart.view.category.editcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.CategoryBinding
import uk.henrytwist.fullcart.databinding.EditCategoryFragmentBinding
import uk.henrytwist.fullcart.view.category.CategoryFragment
import uk.henrytwist.fullcart.view.category.CategoryViewModel

@AndroidEntryPoint
class EditCategoryFragment : CategoryFragment() {

    private lateinit var binding: EditCategoryFragmentBinding

    private val viewModel by viewModels<EditCategoryViewModel>()

    override val categoryViewModel: CategoryViewModel
        get() = viewModel

    private val args by navArgs<EditCategoryFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.args(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = EditCategoryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.observeNavigation(this)

        viewModel.selectedColor.observe(viewLifecycleOwner) {

            getCategoryBinding().addCategoryColors.setSelection(it.ordinal)
        }
    }

    override fun getCategoryBinding(): CategoryBinding {

        return binding.category
    }
}