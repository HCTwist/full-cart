package uk.henrytwist.fullcart.view.list

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.google.android.material.math.MathUtils

class TouchHighlightView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val startRadius = 40F
    private val endRadius = 100F

    private var animationRadius = 0F

    private var baseAlpha = 64
    private val paint = Paint().apply {

        isAntiAlias = true
        color = 0xFF50A2EE.toInt()
    }

    init {

        paint.alpha = baseAlpha

        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.duration = 1250
        animator.startDelay = 3000
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { a ->

            val value = a.animatedValue as Float

            animationRadius = MathUtils.lerp(startRadius, endRadius, value)

            if (value < 0.5) {

                paint.alpha = (alpha * value * 2).toInt()
            } else {

                paint.alpha = (alpha * (1 - ((value - 0.5F) * 2))).toInt()
            }

            invalidate()
        }
        animator.doOnEnd {

            animator.startDelay = 5000
            animator.start()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {

        val cx = width.toFloat() / 2
        val cy = height.toFloat() / 2
        canvas?.drawCircle(cx, cy, animationRadius, paint)
    }
}