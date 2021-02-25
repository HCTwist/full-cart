package uk.henrytwist.fullcart.view.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.fullcart.view.list.navigationmenu.NavigationMenuFragment

abstract class ListFragment : Fragment() {

    abstract val listViewModel: ListViewModel

    protected val headerAdapter = ListHeaderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        headerAdapter.handler = listViewModel

        listViewModel.showHeaderTouchPrompt.observe(viewLifecycleOwner) {

            headerAdapter.showTouchPrompt = it
            headerAdapter.notifyDataSetChanged()
        }

        listViewModel.title.observe(viewLifecycleOwner) {

            headerAdapter.title = it
            headerAdapter.notifyDataSetChanged()
        }

        listViewModel.showNavigationMenu.observeEvent(viewLifecycleOwner) {

            NavigationMenuFragment().show(childFragmentManager, null)
        }
    }
}