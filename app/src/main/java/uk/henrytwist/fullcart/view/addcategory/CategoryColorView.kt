package uk.henrytwist.fullcart.view.addcategory

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import uk.henrytwist.fullcart.R
import kotlin.math.roundToInt

class CategoryColorView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val cornerRadius = resources.getDimension(R.dimen.corner_radius)
    private val borderWidth = resources.getDimension(R.dimen.color_swatch_border)

    private val borderAlpha = 77

    private val swatchPaint = Paint().apply {

        isAntiAlias = true
    }

    private val borderPaint = Paint().apply {

        isAntiAlias = true
        alpha = borderAlpha
    }

    init {

        foreground = ContextCompat.getDrawable(context, R.drawable.color_swatch)
    }

    fun setColor(color: Int) {

        swatchPaint.color = color
        borderPaint.color = ColorUtils.setAlphaComponent(color, 64)
        invalidate()
    }

    fun setSelectionFraction(fraction: Float) {

        borderPaint.alpha = (fraction * borderAlpha).roundToInt()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {

        if (canvas == null) return

        val w = width.toFloat()
        val h = height.toFloat()

        canvas.drawRoundRect(0F, 0F, w, h, cornerRadius + borderWidth, cornerRadius + borderWidth, borderPaint)
        canvas.drawRoundRect(borderWidth, borderWidth, w - borderWidth, h - borderWidth, cornerRadius, cornerRadius, swatchPaint)
    }
}