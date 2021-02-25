package uk.henrytwist.fullcart.view.list.pantry

import androidx.fragment.app.viewModels
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.view.MenuFragment

class PantryMenuFragment : MenuFragment(R.menu.pantry_menu) {

    private val viewModel by viewModels<PantryViewModel>({ requireParentFragment() })

    override fun onNavigationItemSelected(id: Int) {

        when (id) {

            R.id.pantry_delete -> viewModel.onClickDeleteList()
        }
    }
}