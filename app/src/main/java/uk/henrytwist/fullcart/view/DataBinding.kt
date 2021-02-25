package uk.henrytwist.fullcart.view

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration

fun <B : ViewDataBinding> B.interceptNextRebind(bind: B.() -> Unit) {

    addOnRebindCallback(object : OnRebindCallback<B>() {

        override fun onPreBind(binding: B): Boolean {

            bind(binding)
            removeOnRebindCallback(this)
            return false
        }
    })
}

@BindingAdapter("invisibleUnless")
fun invisibleUnless(view: View, boolean: Boolean) {

    view.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, boolean: Boolean) {

    view.visibility = if (boolean) View.VISIBLE else View.GONE
}

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