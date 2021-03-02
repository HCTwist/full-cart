package uk.henrytwist.fullcart.view.components

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.MenuRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import uk.henrytwist.fullcart.R

abstract class MenuFragment(@MenuRes private val menuRes: Int?) : BottomSheetDialogFragment() {

    val navigationView: NavigationView
        get() = requireView().findViewById(R.id.menu_navigation_view)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (menuRes != null) navigationView.inflateMenu(menuRes)

        navigationView.setNavigationItemSelectedListener {

            onNavigationItemSelected(it.itemId)
            dismiss()
            true
        }
    }

    abstract fun onNavigationItemSelected(id: Int)
}