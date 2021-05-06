package com.arturo.ru.skillbranch.skillarticles.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.arturo.ru.skillbranch.skillarticles.R
import com.arturo.ru.skillbranch.skillarticles.extensions.getIntDimension
import com.arturo.ru.skillbranch.skillarticles.viewmodels.ArticleState
import com.arturo.ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import com.arturo.ru.skillbranch.skillarticles.viewmodels.Notification
import com.arturo.ru.skillbranch.skillarticles.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_article_submenu.*
import kotlinx.android.synthetic.main.layout_bottombar.*

@SuppressLint("UseCompatLoadingForDrawables")
class RootActivity : AppCompatActivity(R.layout.activity_root) {

    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupBottombar()
        setupSubmenu()

        val vmFactory = ViewModelFactory("0")
        viewModel = ViewModelProvider(this, vmFactory).get(ArticleViewModel::class.java)

        viewModel.observeState(this) {
            renderUi(it)
        }
        viewModel.observeNotifications(this) {
            renderNotification(it)
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

    private fun setupBottombar() {
        btn_like.setOnClickListener { viewModel.handleLike() }
        btn_bookmark.setOnClickListener { viewModel.handleBookmark() }
        btn_share.setOnClickListener { viewModel.handleShare() }
        btn_settings.setOnClickListener { viewModel.handleToggleMenu() }
    }

    private fun setupSubmenu() {
        btn_text_up.setOnClickListener { viewModel.handleUpText() }
        btn_text_down.setOnClickListener { viewModel.handleDownText() }
        switch_mode.setOnClickListener { viewModel.handleSwitchMode() }
    }

    private fun renderUi(data: ArticleState) {
        // bind submenu state
        btn_settings.isChecked = data.isShowMenu
        if (data.isShowMenu) submenu.open() else submenu.close()

        // bind article personal data
        btn_like.isChecked = data.isLike
        btn_bookmark.isChecked = data.isBookmark

        // bind submenu views
        switch_mode.isChecked = data.isDarkMode
        delegate.localNightMode = if (data.isDarkMode) MODE_NIGHT_YES else MODE_NIGHT_NO

        if (data.isBigText) {
            tv_text_content.textSize = 18f
            btn_text_up.isChecked = true
            btn_text_down.isChecked = false
        } else {
            tv_text_content.textSize = 14f
            btn_text_up.isChecked = false
            btn_text_down.isChecked = true
        }

        // bind content
        tv_text_content.text = if (data.isLoadingContent) "Loading" else data.content.first() as String

        // bind toolbar
        toolbar.title = data.title ?: "loading"
        toolbar.subtitle = data.category ?: "loading"
        if (data.categoryIcon != null) toolbar.logo = getDrawable(data.categoryIcon as Int)
    }

    private fun renderNotification(notification: Notification) {
        val snackbar = Snackbar.make(coordinator_container, notification.message, Snackbar.LENGTH_LONG)
                .setAnchorView(bottombar)
                .setActionTextColor(getColor(R.color.color_accent_dark))

        when (notification) {

            is Notification.TextMessage -> { /* nothing */ }

            is Notification.ActionMessage -> {
                with(snackbar) {
                    setActionTextColor(getColor(R.color.color_accent_dark))
                    setAction(notification.actionLabel) {
                        notification.actionHandler?.invoke()
                    }
                }
            }

            is Notification.ErrorMessage -> {
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    snackbar.setAction(notification.errorLabel) {
                        notification.errorHandler?.invoke()
                    }
                }
            }
        }

        snackbar.show()
    }
}