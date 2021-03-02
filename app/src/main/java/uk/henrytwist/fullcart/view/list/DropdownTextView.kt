package uk.henrytwist.fullcart.view.list

import android.content.Context
import android.text.SpannableString
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import uk.henrytwist.fullcart.R

class DropdownTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    override fun setText(text: CharSequence?, type: BufferType?) {

        val resolvedText = text ?: ""
        val string = SpannableString("$resolvedText  ")

        val icon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_drop_down_24)
        val iconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        icon?.setBounds(0, 0, iconSize, iconSize)
        val span = VerticallyCenteredImageSpan(icon)

        string.setSpan(span, resolvedText.length, resolvedText.length + 1, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)

        super.setText(string)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val maxLines = maxLines
        if (maxLines == -1) return

        val availableWidth = measuredWidth.toFloat() - compoundPaddingLeft - compoundPaddingRight
        val availableSpace = maxLines * availableWidth

        val ellipsizedText = TextUtils.ellipsize(text, paint, availableWidth, TextUtils.TruncateAt.END)

        if(ellipsizedText != text) {

//            val realAvailableWidth =
//            val reEllipsizedText =
        }
    }
}