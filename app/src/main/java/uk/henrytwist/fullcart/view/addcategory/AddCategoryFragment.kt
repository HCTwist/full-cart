package uk.henrytwist.fullcart.view.addcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.AddCategoryFragmentBinding
import uk.henrytwist.fullcart.view.showSoftKeyboard
import uk.henrytwist.selectslider.SelectSliderView

@AndroidEntryPoint
class AddCategoryFragment : Fragment() {

    private lateinit var binding: AddCategoryFragmentBinding

    private val viewModel by viewModels<AddCategoryViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddCategoryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        binding.addCategoryName.showSoftKeyboard()

        val colorAdapter = CategoryColorAdapter()

        binding.addCategoryColors.run {

            setHasFixedSize(true)
            adapter = colorAdapter
            onSelectListener = SelectSliderView.OnSelectListener {

                viewModel.onColorSelected(it)
            }
        }

        binding.addCategoryDefault.setOnClickListener {

            viewModel.onDefaultCheckChanged(binding.addCategoryDefault.isChecked)
        }
    }
}