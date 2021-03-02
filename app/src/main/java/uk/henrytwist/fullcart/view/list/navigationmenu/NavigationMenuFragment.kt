package uk.henrytwist.fullcart.view.list.navigationmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import uk.henrytwist.fullcart.R
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.view.components.MenuFragment

@AndroidEntryPoint
class NavigationMenuFragment : MenuFragment(R.menu.list_menu) {

    private val viewModel by viewModels<NavigationMenuViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.observeNavigation(requireParentFragment())
        viewModel.init(navigationView.menu)
    }

    override fun onNavigationItemSelected(id: Int) {

        viewModel.onItemSelected(id)
    }
}