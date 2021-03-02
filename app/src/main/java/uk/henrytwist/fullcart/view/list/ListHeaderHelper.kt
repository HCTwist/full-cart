package uk.henrytwist.fullcart.view.list

import android.content.Context
import android.text.SpannableString
import androidx.appcompat.widget.DialogTitle
import androidx.core.content.ContextCompat
import uk.henrytwist.fullcart.R

object ListHeaderHelper {

    fun addDropdownIcon(context: Context, title: String): SpannableString {

        val string = SpannableString("$title  ")

        val icon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_drop_down_24)
        val iconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        icon?.setBounds(0, 0, iconSize, iconSize)
        val span = VerticallyCenteredImageSpan(icon)

        string.setSpan(span, title.length, title.length + 1, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)

        return string
    }
}