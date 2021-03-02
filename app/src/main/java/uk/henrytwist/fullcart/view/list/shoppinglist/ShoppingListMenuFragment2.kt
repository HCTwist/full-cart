package uk.henrytwist.fullcart.view.list.shoppinglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.navigationmenuview.NavigationMenuView
import uk.henrytwist.fullcart.R
import uk.henrytwist.fullcart.view.components.MenuFragment2

// TODO Finish
@AndroidEntryPoint
class ShoppingListMenuFragment2 : MenuFragment2() {

    private val viewModel by viewModels<ShoppingListViewModel>({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildMenu {

            addItem(R.string.list_delete, R.drawable.outline_delete_forever_24) {

                viewModel.onClickDeleteList()
            }

            addItem(R.string.list_share, R.drawable.outline_share_24) {

                viewModel.onShareClicked()
            }

            val testItem = NavigationMenuView.MenuItem.Item("Test", null, true) {}
            addItem(testItem)
        }
    }
}