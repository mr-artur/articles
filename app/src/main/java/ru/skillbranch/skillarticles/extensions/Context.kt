package ru.skillbranch.skillarticles.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

fun Context.dpToPx(dp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )
}

fun Context.dpToIntPx(dp: Int): Int {
    return dpToPx(dp).toInt()
}

fun Context.getIntDimension(@DimenRes dimensionId: Int): Int {
    return getDimension(dimensionId).toInt()
}

fun Context.getDimension(@DimenRes dimensionId: Int): Float {
    return resources.getDimension(dimensionId)
}