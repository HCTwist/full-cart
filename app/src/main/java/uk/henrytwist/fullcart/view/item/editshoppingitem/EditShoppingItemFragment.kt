package uk.henrytwist.fullcart.view.item.editshoppingitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.EditShoppingItemFragmentBinding
import uk.henrytwist.fullcart.databinding.ItemBinding
import uk.henrytwist.fullcart.view.item.ItemFragment
import uk.henrytwist.fullcart.view.item.ItemViewModel

@AndroidEntryPoint
class EditShoppingItemFragment : ItemFragment() {

    private lateinit var binding: EditShoppingItemFragmentBinding

    private val viewModel by viewModels<EditShoppingItemViewModel>()

    override val itemViewModel: ItemViewModel
        get() = viewModel

    private val navArgs by navArgs<EditShoppingItemFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.args(navArgs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = EditShoppingItemFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.pantrySelectionDialogHelper.observeShowSelectionDialog(requireContext(), viewLifecycleOwner)
        viewModel.shoppingListSelectionDialogHelper.observeShowSelectionDialog(requireContext(), viewLifecycleOwner)
    }

    override fun getEditItemBinding(): ItemBinding {

        return binding.pantryItem
    }
}