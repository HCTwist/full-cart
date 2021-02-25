package uk.henrytwist.fullcart.view.list.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.fullcart.databinding.PantryFragmentBinding
import uk.henrytwist.fullcart.view.list.ListFragment
import uk.henrytwist.fullcart.view.list.ListLoadingAdapter
import uk.henrytwist.fullcart.view.list.ListViewModel

@AndroidEntryPoint
class PantryFragment : ListFragment() {

    private lateinit var binding: PantryFragmentBinding

    private val viewModel by viewModels<PantryViewModel>()
    override val listViewModel: ListViewModel
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.args(requireArguments())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = PantryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.observeNavigation(this)

        viewModel.showMenu.observeEvent(viewLifecycleOwner) {

            PantryMenuFragment().show(childFragmentManager, null)
        }

        val itemsAdapter = PantryAdapter()
        itemsAdapter.handler = viewModel

        val loadingItemsAdapter = ListLoadingAdapter()

        val loadingAdapter = ConcatAdapter(headerAdapter, loadingItemsAdapter)
        val listAdapter = ConcatAdapter(headerAdapter, itemsAdapter)

        binding.pantryRecycler.run {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.loading.observe(viewLifecycleOwner) {

            binding.pantryRecycler.adapter = if (it) loadingAdapter else listAdapter
        }

        viewModel.items.observe(viewLifecycleOwner) {

            itemsAdapter.submitList(it)
        }
    }
}