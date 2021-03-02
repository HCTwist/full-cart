package uk.henrytwist.fullcart.view.item.addpantryitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.databinding.AddPantryItemFragmentBinding
import uk.henrytwist.fullcart.databinding.ItemBinding
import uk.henrytwist.fullcart.view.item.ItemViewModel
import uk.henrytwist.fullcart.view.item.SearchableItemFragment
import uk.henrytwist.fullcart.view.pantryitem.UseByDatePickerHelper
import uk.henrytwist.fullcart.view.showSoftKeyboard

@AndroidEntryPoint
class AddPantryItemFragment : SearchableItemFragment() {

    lateinit var binding: AddPantryItemFragmentBinding

    private val viewModel by viewModels<AddPantryItemViewModel>()

    override val itemViewModel: ItemViewModel
        get() = viewModel

    private val args by navArgs<AddPantryItemFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) viewModel.args(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddPantryItemFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.pantryItem.item.editItemNameContainer.showSoftKeyboard()

        UseByDatePickerHelper.setup(this, viewModel)
    }

    override fun getItemBinding(): ItemBinding {

        return binding.pantryItem.item
    }

    override fun getSearchRecycler(): RecyclerView {

        return binding.pantryItem.item.editItemSearch
    }
}