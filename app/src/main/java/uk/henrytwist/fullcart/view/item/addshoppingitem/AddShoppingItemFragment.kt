package uk.henrytwist.fullcart.view.item.addshoppingitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.AddShoppingItemFragmentBinding
import uk.henrytwist.fullcart.databinding.ItemBinding
import uk.henrytwist.fullcart.view.item.ItemViewModel
import uk.henrytwist.fullcart.view.item.SearchableItemFragment
import uk.henrytwist.fullcart.view.showSoftKeyboard

@AndroidEntryPoint
class AddShoppingItemFragment : SearchableItemFragment() {

    private val viewModel by viewModels<AddShoppingItemViewModel>()

    override val itemViewModel: ItemViewModel
        get() = viewModel

    private val args by navArgs<AddShoppingItemFragmentArgs>()

    private lateinit var binding: AddShoppingItemFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) viewModel.args(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddShoppingItemFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.item.editItemNameContainer.showSoftKeyboard()
    }

    override fun getItemBinding(): ItemBinding {

        return binding.item
    }

    override fun getSearchRecycler(): RecyclerView {

        return binding.item.editItemSearch
    }
}