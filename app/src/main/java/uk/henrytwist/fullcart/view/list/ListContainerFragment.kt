package uk.henrytwist.fullcart.view.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.models.ListType
import uk.henrytwist.fullcart.view.list.nolist.NoListFragment
import uk.henrytwist.fullcart.view.list.pantry.PantryFragment
import uk.henrytwist.fullcart.view.list.shoppinglist.ShoppingListFragment

@AndroidEntryPoint
class ListContainerFragment : Fragment(R.layout.list_container_fragment) {

    private val viewModel by viewModels<ListContainerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.args(arguments)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observeEvent(viewLifecycleOwner) {

            val fragment = when (it.type) {

                null -> NoListFragment()
                ListType.SHOPPING_LIST -> ShoppingListFragment()
                ListType.PANTRY -> PantryFragment()
            }
            fragment.arguments = it.args

            childFragmentManager.beginTransaction().replace(R.id.list_container, fragment).commit()
        }
    }
}