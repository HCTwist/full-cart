package uk.henrytwist.fullcart.view.components.strikeview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.ColorUtils
import androidx.core.widget.addTextChangedListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import uk.henrytwist.fullcart.R
import kotlin.math.min
import kotlin.math.pow


class StrikeTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    private var _struck: Boolean? = null
    private val struck: Boolean
        get() = _struck ?: false

    private var strikeProgress = 0F

    private val lineBounds = LineBounds()

    private val textColor = textColors.getColorForState(drawableState, textColors.defaultColor)
    private val strikeColor = textColors.getColorForState(intArrayOf(android.R.attr.state_checked), textColors.defaultColor)

    private val linePaint = Paint().apply {

        color = strikeColor
        strokeWidth = resources.getDimension(R.dimen.text_strike_height)
    }

    private var strikeAnimator: ValueAnimator? = null
    private val baseStrikeAnimationDuration = 5000L

    init {

        addTextChangedListener {

            lineBounds.invalidate()
        }
    }

    fun setStruck(struck: Boolean) {

        if (this.struck != struck) {

            val shouldAnimate = _struck != null && false // TODO RecyclerView animation
            _struck = struck

            val newProgress = if (struck) 1F else 0F
            strikeTo(newProgress, shouldAnimate)
        }
    }

    private fun strikeTo(progress: Float, animate: Boolean) {

        strikeAnimator?.cancel()

        if (animate) {

            if (strikeAnimator == null) {

                strikeAnimator = ValueAnimator().apply {

                    interpolator = FastOutSlowInInterpolator()

                    addUpdateListener {

                        setStrikeAnimationProgress(it.animatedValue as Float)
                    }
                }
            }

            strikeAnimator?.let {

                it.duration = (baseStrikeAnimationDuration * lineCount.toFloat().pow(0.7F)).toLong()
                it.setFloatValues(strikeProgress, progress)
                it.start()
            }
        } else {

            setStrikeAnimationProgress(progress)
        }
    }

    private fun setStrikeAnimationProgress(progress: Float) {

        strikeProgress = progress
        invalidateTextColor()
        invalidate()
    }

    private fun invalidateTextColor() {

        setTextColor(ColorUtils.blendARGB(textColor, strikeColor, strikeProgress))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let { c ->

            val rtlSave = c.save()
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {

                c.scale(-1F, 1F, width.toFloat() / 2, height.toFloat() / 2)
            }

            var lineWidthToDraw = 0F

            for (i in 0 until lineCount) {

                lineWidthToDraw += layout.getLineWidth(i)
            }

            lineWidthToDraw *= strikeProgress

            for (i in 0 until lineCount) {

                if (lineWidthToDraw <= 0) break

                val bounds = lineBounds.getBoundsFor(this, i)
                val startX = bounds.left.toFloat()
                val width = layout.getLineWidth(i)
                val lineCy = bounds.exactCenterY()

                c.drawLine(
                        startX,
                        lineCy,
                        startX + min(width, lineWidthToDraw),
                        lineCy,
                        linePaint
                )

                lineWidthToDraw -= width
            }

            c.restoreToCount(rtlSave)
        }
    }

    class LineBounds {

        private var bounds: ArrayList<Bounds>? = null

        fun invalidate() {

            bounds?.forEach { it.dirty = true }
        }

        fun getBoundsFor(textView: TextView, line: Int): Rect {

            ensureLineCount(textView.lineCount)

            val b = bounds!![line]
            if (b.dirty) {

                val r = bounds!![line].rect
                textView.getLineBounds(line, r)
                b.dirty = false
            }

            return b.rect
        }

        private fun ensureLineCount(count: Int) {

            if (bounds == null) {

                bounds = ArrayList(count)
                repeat(count) {

                    bounds!!.add(Bounds())
                }
            } else if (count > bounds!!.size) {

                bounds!!.ensureCapacity(count)
                repeat(count - bounds!!.size) {

                    bounds!!.add(Bounds())
                }
            }
        }

        class Bounds {

            val rect: Rect = Rect()
            var dirty: Boolean = true
        }
    }
}