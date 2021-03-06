package ru.skillbranch.skillarticles.markdown.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

class BlockquotesSpan(
    @Px private val gapWidth: Float,
    @Px private val quoteWidth: Float,
    @ColorInt private val lineColor: Int
) : LeadingMarginSpan {

    override fun getLeadingMargin(first: Boolean): Int {
        return (quoteWidth + gapWidth).toInt()
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
        paint?.withCustomStyles {
            canvas?.drawLine(
                quoteWidth / 2,
                lineTop.toFloat(),
                quoteWidth / 2,
                lineBottom.toFloat(),
                paint
            )
        }
    }

    private inline fun Paint.withCustomStyles(block: () -> Unit) {
        val oldColor = color
        val oldStyle = style
        val oldWidth = strokeWidth

        color = lineColor
        style = Paint.Style.STROKE
        strokeWidth = quoteWidth

        block()

        color = oldColor
        style = oldStyle
        strokeWidth = oldWidth
    }
}