package uk.henrytwist.fullcart.view.addlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.AddListFragmentBinding
import uk.henrytwist.fullcart.view.showSoftKeyboard

@AndroidEntryPoint
class AddListFragment : Fragment() {

    private val viewModel by viewModels<AddListViewModel>()

    private lateinit var binding: AddListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        binding.addListType.setOnCheckedChangeListener { _, checkedId ->

            viewModel.onTypeChanged(checkedId)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.addShoppingListName.showSoftKeyboard()
    }
}