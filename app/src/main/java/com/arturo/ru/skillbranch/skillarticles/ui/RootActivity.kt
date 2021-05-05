package com.arturo.ru.skillbranch.skillarticles.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import com.arturo.ru.skillbranch.skillarticles.R
import com.arturo.ru.skillbranch.skillarticles.extensions.getIntDimension
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_article_submenu.*
import kotlinx.android.synthetic.main.layout_bottombar.*

class RootActivity : AppCompatActivity(R.layout.activity_root) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        btn_like.setOnClickListener {
            Snackbar.make(coordinator_container, "hello", Snackbar.LENGTH_LONG)
                .setAnchorView(bottombar)
                .show()
        }

        switch_mode.setOnClickListener {
            delegate.localNightMode = if (switch_mode.isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO
        }
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