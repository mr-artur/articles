package ru.skillbranch.skillarticles.ui.delegates

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class AttrValue(@AttrRes private val resId: Int) : ReadOnlyProperty<Context, Int> {

    private var value: Int? = null

    override fun getValue(thisRef: Context, property: KProperty<*>): Int {
        if (value == null) {
            val tv = TypedValue()
            if (thisRef.theme.resolveAttribute(resId, tv, true)) value = tv.data
            else throw Resources.NotFoundException("Resource with id $resId not found")
        }
        return value!!
    }
}