package uk.henrytwist.fullcart.view.list.nolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.fullcart.databinding.NoListFragmentBinding
import uk.henrytwist.fullcart.view.list.navigationmenu.NavigationMenuFragment

@AndroidEntryPoint
class NoListFragment : Fragment() {

    private lateinit var binding: NoListFragmentBinding

    private val viewModel by viewModels<NoListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = NoListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        viewModel.showNavigationMenu.observeEvent(viewLifecycleOwner) {

            NavigationMenuFragment().show(childFragmentManager, null)
        }
    }
}