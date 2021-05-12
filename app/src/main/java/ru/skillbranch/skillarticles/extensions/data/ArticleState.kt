package ru.skillbranch.skillarticles.extensions.data

import ru.skillbranch.skillarticles.data.AppSettings
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.viewmodels.ArticleState
import ru.skillbranch.skillarticles.viewmodels.BottombarData
import ru.skillbranch.skillarticles.viewmodels.SubmenuData

fun ArticleState.toAppSettings(): AppSettings {
    return AppSettings(isDarkMode, isBigText)
}

fun ArticleState.toArticlePersonalInfo(): ArticlePersonalInfo {
    return ArticlePersonalInfo(isLike, isBookmark)
}

fun ArticleState.toBottombarData(): BottombarData {
    return BottombarData(
        isLike,
        isBookmark,
        isShowMenu,
        isSearch,
        searchResults.size,
        searchPosition
    )
}

fun ArticleState.toSubmenuData(): SubmenuData {
    return SubmenuData(isShowMenu, isBigText, isDarkMode)
}