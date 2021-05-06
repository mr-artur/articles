package com.arturo.ru.skillbranch.skillarticles.extensions.data

import com.arturo.ru.skillbranch.skillarticles.data.AppSettings
import com.arturo.ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import com.arturo.ru.skillbranch.skillarticles.viewmodels.ArticleState

fun ArticleState.toAppSettings(): AppSettings {
    return AppSettings(isDarkMode, isBigText)
}

fun ArticleState.toArticlePersonalInfo(): ArticlePersonalInfo {
    return ArticlePersonalInfo(isLike, isBookmark)
}