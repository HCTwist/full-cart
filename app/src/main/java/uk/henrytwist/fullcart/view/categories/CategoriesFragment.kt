package uk.henrytwist.fullcart.view.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.databinding.CategoriesFragmentBinding
import uk.henrytwist.fullcart.view.components.HeaderAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.categories_fragment) {

    private val viewModel by viewModels<CategoriesViewModel>()

    private lateinit var binding: CategoriesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = CategoriesFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        val headerAdapter = HeaderAdapter()
        headerAdapter.title = getString(R.string.categories_title)
        headerAdapter.handler = viewModel

        val categoriesAdapter = CategoriesAdapter()
        categoriesAdapter.handler = viewModel

        val listAdapter = ConcatAdapter(headerAdapter, categoriesAdapter)

        binding.categoriesRecycler.run {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel.categories.observe(viewLifecycleOwner) {

            categoriesAdapter.submitList(it)
        }
    }
}