package uk.henrytwist.fullcart.view.list.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.fullcart.databinding.ShoppingListFragmentBinding
import uk.henrytwist.fullcart.view.list.ListFragment
import uk.henrytwist.fullcart.view.list.ListLoadingAdapter
import uk.henrytwist.fullcart.view.list.ListViewModel

@AndroidEntryPoint
class ShoppingListFragment : ListFragment() {

    private lateinit var binding: ShoppingListFragmentBinding

    private val viewModel by viewModels<ShoppingListViewModel>()
    override val listViewModel: ListViewModel
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.args(requireArguments())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = ShoppingListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.observeNavigation(this)

        viewModel.showMenu.observeEvent(viewLifecycleOwner) {

            ShoppingListMenuFragment().show(childFragmentManager, null)
        }

        viewModel.pantrySelectionDialogHelper.observeShowSelectionDialog(requireContext(), viewLifecycleOwner)

        val itemsAdapter = ShoppingItemsAdapter()
        itemsAdapter.handler = viewModel

        val loadingItemsAdapter = ListLoadingAdapter()

        val listAdapter = ConcatAdapter(headerAdapter, itemsAdapter)
        val loadingAdapter = ConcatAdapter(headerAdapter, loadingItemsAdapter)

        binding.shoppingListRecycler.run {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.loading.observe(viewLifecycleOwner) {

            binding.shoppingListRecycler.adapter = if (it) loadingAdapter else listAdapter
        }

        viewModel.rows.observe(viewLifecycleOwner) {

            itemsAdapter.submitList(it)
        }
    }
}