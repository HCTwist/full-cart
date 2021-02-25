package uk.henrytwist.fullcart.view.list.shoppinglist

import androidx.fragment.app.viewModels
import uk.henrytwist.fullcart.R
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.fullcart.view.MenuFragment

@AndroidEntryPoint
class ShoppingListMenuFragment : MenuFragment(R.menu.shopping_list_menu) {

    private val viewModel by viewModels<ShoppingListViewModel>({ requireParentFragment() })

    override fun onNavigationItemSelected(id: Int) {

        when (id) {

            R.id.shopping_list_delete -> viewModel.onClickDeleteList()
        }
    }
}