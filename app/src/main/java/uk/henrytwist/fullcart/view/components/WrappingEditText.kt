package uk.henrytwist.fullcart.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import uk.henrytwist.androidbasics.views.KeyboardCompliantEditText

class WrappingEditText(context: Context, attributeSet: AttributeSet) : KeyboardCompliantEditText(context, attributeSet) {

    init {

        maxLines = Integer.MAX_VALUE
        setHorizontallyScrolling(false)
        imeOptions = EditorInfo.IME_ACTION_DONE
    }
}