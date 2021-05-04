package com.arturo.ru.skillbranch.skillarticles.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.arturo.ru.skillbranch.skillarticles.R
import com.arturo.ru.skillbranch.skillarticles.extensions.getIntDimension
import kotlinx.android.synthetic.main.activity_root.*

class RootActivity : AppCompatActivity(R.layout.activity_root) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val logo = if (toolbar.childCount > 2) toolbar.getChildAt(2) as ImageView else null
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let {
            it.width = getIntDimension(R.dimen.icon_size_normal_40)
            it.height = getIntDimension(R.dimen.icon_size_normal_40)
            it.marginEnd = getIntDimension(R.dimen.spacing_normal_16)
            logo.layoutParams = it
        }
    }
}