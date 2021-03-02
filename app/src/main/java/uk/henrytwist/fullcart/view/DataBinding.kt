package uk.henrytwist.fullcart.view

import android.content.res.Configuration
import android.graphics.Paint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration

// TODO Work in progress
@BindingAdapter("fadeGoneUnless")
fun fadeGoneUnless(view: View, boolean: Boolean) {

    val tagKey = "fade_gone_unless_initialised".hashCode()

    val endVisibility = if (boolean) View.VISIBLE else View.GONE

    if (view.visibility == endVisibility) return

    if (view.getTag(tagKey) == true) {

        view.visibility = View.VISIBLE
        view.alpha = if (boolean) 0F else 1F
        view.animate().run {

            duration = 1500
            alpha(if (boolean) 1F else 0F)
            withEndAction {

                view.visibility = endVisibility
            }
        }
    } else {

        view.visibility = endVisibility
        view.setTag(tagKey, true)
    }
}

@BindingAdapter("itemMargins")
fun setItemMargins(recyclerView: RecyclerView, margin: Float) {

    recyclerView.addItemDecoration(MarginItemDecoration(margin))
}

@BindingAdapter("strike")
fun setStrike(textView: TextView, boolean: Boolean) {

    if (boolean) {

        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {

        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("goneIfLandscapeOr")
fun goneIfLandscapeOr(view: View, boolean: Boolean) {

    if (view.isLandscape() || boolean) {

        view.visibility = View.GONE
    } else {

        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("deviceMatchingOrientation")
fun deviceMatchingOrientation(linearLayout: LinearLayout, boolean: Boolean) {

    linearLayout.orientation = if (linearLayout.isLandscape()) {

        LinearLayout.HORIZONTAL
    } else {

        LinearLayout.VERTICAL
    }
}

private fun View.isLandscape() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE