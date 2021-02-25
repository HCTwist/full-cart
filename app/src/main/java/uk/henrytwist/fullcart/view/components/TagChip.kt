package uk.henrytwist.fullcart.view.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import com.google.android.material.chip.Chip
import uk.henrytwist.androidbasics.getColorAttr
import uk.henrytwist.fullcart.R

class TagChip(context: Context, attributeSet: AttributeSet) : Chip(context, attributeSet) {

    init {

        rippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        stateListAnimator = null
        setEnsureMinTouchTargetSize(false)

        context.obtainStyledAttributes(attributeSet, R.styleable.TagChip).apply {

            setTagColor(getColor(R.styleable.TagChip_tagColor, 0))
            recycle()
        }
    }

    fun setTagColor(color: Int) {

        setTextColor(color)
        chipBackgroundColor = ColorStateList.valueOf(resolveBackgroundColor(context, color))
    }

    companion object {

        fun resolveBackgroundColor(context: Context, color: Int): Int {

            val colorBackground = context.getColorAttr(android.R.attr.colorBackground)
            val alphaConsumedColor = ColorUtils.compositeColors(color, colorBackground)
            return ColorUtils.blendARGB(colorBackground, alphaConsumedColor, 0.15F)
        }
    }
}