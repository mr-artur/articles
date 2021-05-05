package com.arturo.ru.skillbranch.skillarticles.ui.custom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.arturo.ru.skillbranch.skillarticles.R
import com.arturo.ru.skillbranch.skillarticles.extensions.getDimension
import com.google.android.material.shape.MaterialShapeDrawable
import kotlin.math.hypot

class ArticleSubmenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isOpen: Boolean = false
    private var centerX: Float = context.getDimension(R.dimen.submenu_width)
    private var centerY: Float = context.getDimension(R.dimen.submenu_height)

    init {
        View.inflate(context, R.layout.layout_article_submenu, this)
        val materialBg = MaterialShapeDrawable.createWithElevationOverlay(context)
        materialBg.elevation = elevation
        background = materialBg
    }

    fun open() {
        if (isOpen || !isAttachedToWindow) return
        isOpen = true
        animateShow()
    }

    fun close() {
        if (!isOpen || !isAttachedToWindow) return
        isOpen = false
        animateHide()
    }

    private fun animateShow() {
        val endRadius = hypot(centerX, centerY).toInt()
        val anim = ViewAnimationUtils.createCircularReveal(
            this,
            centerX.toInt(),
            centerY.toInt(),
            0f,
            endRadius.toFloat()
        )
        anim.doOnStart {
            visibility = View.VISIBLE
        }
        anim.start()
    }

    private fun animateHide() {
        val endRadius = hypot(centerX, centerY).toInt()
        val anim = ViewAnimationUtils.createCircularReveal(
            this,
            centerX.toInt(),
            centerY.toInt(),
            endRadius.toFloat(),
            0f
        )
        anim.doOnEnd {
            visibility = View.GONE
        }
        anim.start()
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isOpen = isOpen
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            isOpen = state.isOpen
            visibility = if (isOpen) View.VISIBLE else View.GONE
        }
    }

    private class SavedState : BaseSavedState, Parcelable {

        var isOpen: Boolean = false

        constructor(superState: Parcelable?) : super(superState)

        constructor(src: Parcel) : super(src) {
            isOpen = src.readInt() == 1
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(if (isOpen) 1 else 0)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {

            override fun createFromParcel(source: Parcel): SavedState = SavedState(source)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}