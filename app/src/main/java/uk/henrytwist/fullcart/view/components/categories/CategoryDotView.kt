package uk.henrytwist.fullcart.view.components.categories

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import uk.henrytwist.fullcart.R

class CategoryDotView(context: Context, attributeSet: AttributeSet) : AppCompatImageView(context, attributeSet) {

    init {

        setImageResource(R.drawable.dot)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val size = resources.getDimensionPixelSize(R.dimen.category_dot_size)
        setMeasuredDimension(size, size)
    }

    fun setColor(color: Int?) {

        imageTintList = ColorStateList.valueOf(color
                ?: ContextCompat.getColor(context, R.color.no_category))
    }
}