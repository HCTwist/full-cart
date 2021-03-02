package uk.henrytwist.fullcart.view.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.henrytwist.androidbasics.navigationmenuview.NavigationMenuView
import uk.henrytwist.fullcart.R

abstract class MenuFragment2 : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.menu_fragment_2, container, false)
    }

    fun buildMenu(buildAction: NavigationMenuView.Builder.() -> Unit) {

        (view as NavigationMenuView).buildMenu(buildAction)
    }
}