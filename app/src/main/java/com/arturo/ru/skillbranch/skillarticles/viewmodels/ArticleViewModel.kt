package com.arturo.ru.skillbranch.skillarticles.viewmodels

import com.arturo.ru.skillbranch.skillarticles.data.repositories.ArticleRepository

class ArticleViewModel(articleId: String) : BaseViewModel<ArticleState>(ArticleState()) {

    private val repository = ArticleRepository

    init {

    }

    fun handleSwitchMode() {
        updateState { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun handleDownText() {
        TODO("Not yet implemented")
    }

    fun handleUpText() {
        TODO("Not yet implemented")
    }

    fun handleToggleMenu() {
        updateState { it.copy(isShowMenu = !it.isShowMenu) }
    }

    fun handleShare() {
        TODO("Not yet implemented")
    }

    fun handleBookmark() {
        TODO("Not yet implemented")
    }

    fun handleLike() {
        TODO("Not yet implemented")
    }
}

data class ArticleState(
    val isAuth: Boolean = false,                           // пользователь авторизован
    val isLoadingContent: Boolean = true,                  // контент загружается
    val isLoadingReviews: Boolean = true,                  // отзывы загружаются
    val isLike: Boolean = false,                           // отмечено как понравившееся
    val isBookmark: Boolean = false,                       // добавлено в закладки
    val isShowMenu: Boolean = false,                       // меню отображается
    val isBigText: Boolean = false,                        // шрифт увеличен
    val isDarkMode: Boolean = false,                       // темный режим
    val isSearch: Boolean = false,                         // режим поиска
    val searchQuery: String? = null,                       // поисковый запрос
    val searchResults: List<Pair<Int, Int>> = emptyList(), // результаты поиска (стартовая и конечная позиции)
    val searchPosition: Int = 0,                           // текущая позиция найденного результата
    val shareLink: String? = null,                         // ссылка на share
    val title: String? = null,                             // заголовок статьи
    val category: String? = null,                          // категория
    val categoryIcon: Any? = null,                         // иконка категории
    val date: String? = null,                              // дата публикации
    val author: Any? = null,                               // автор статьи
    val poster: String? = null,                            // обложка статьи
    val content: List<Any> = emptyList(),                  // контент
    val reviews: List<Any> = emptyList()                   // комментарии
)