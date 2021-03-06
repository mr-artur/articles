package ru.skillbranch.skillarticles.markdown.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

class OrderedListSpan(
    @Px private val gapWidth: Float,
    private val order: String,
    @ColorInt private val orderColor: Int
) : LeadingMarginSpan {

    override fun getLeadingMargin(first: Boolean): Int {
        return (2 * gapWidth).toInt()
    }

    override fun drawLeadingMargin(
        canvas: Canvas?,
        paint: Paint?,
        currentMarginLocation: Int,
        paragraphDirection: Int,
        lineTop: Int,
        lineBaseline: Int,
        lineBottom: Int,
        text: CharSequence?,
        lineStart: Int,
        lineEnd: Int,
        isFirstLine: Boolean,
        layout: Layout?
    ) {
        if (isFirstLine) {
            paint?.withCustomColor {
                canvas?.drawText(
                    order,
                    currentMarginLocation + gapWidth,
                    lineBaseline.toFloat(),
                    paint
                )
            }
        }
    }

    private inline fun Paint.withCustomColor(block: () -> Unit) {
        val oldColor = color
        color = orderColor

        block()

        color = oldColor
    }
}