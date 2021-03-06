package ru.skillbranch.skillarticles.viewmodels

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.repositories.ArticleRepository
import ru.skillbranch.skillarticles.extensions.*

class ArticleViewModel(private val articleId: String, savedStateHandle: SavedStateHandle) :
    BaseViewModel<ArticleState>(ArticleState(), savedStateHandle), IArticleViewModel {

    private val repository = ArticleRepository()

    init {
        // set custom saved state provider for non-serializable or custom states
        savedStateHandle.setSavedStateProvider(KEY_STATE) { currentState.toBundle() }

        subscribeOnDataSource(getArticleData()) { article, state ->
            article ?: return@subscribeOnDataSource null
            state.copy(
                shareLink = article.shareLink,
                title = article.title,
                category = article.category,
                categoryIcon = article.categoryIcon,
                date = article.date.format(),
                author = article.author
            )
        }

        subscribeOnDataSource(getArticleContent()) { content, state ->
            content ?: return@subscribeOnDataSource null
            state.copy(
                isLoadingContent = false,
                content = content
            )
        }

        subscribeOnDataSource(getArticlePersonalInfo()) { info, state ->
            info ?: return@subscribeOnDataSource null
            state.copy(
                isBookmark = info.isBookmark,
                isLike = info.isLike
            )
        }

        subscribeOnDataSource(repository.getAppSettings()) { settings, state ->
            state.copy(
                isDarkMode = settings.isDarkMode,
                isBigText = settings.isBigText
            )
        }
    }

    override fun handleNightMode() {
        val settings = currentState.toAppSettings()
        repository.updateSettings(settings.copy(isDarkMode = !settings.isDarkMode))
    }

    override fun handleDownText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = false))
    }

    override fun handleUpText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = true))
    }

    override fun handleToggleMenu() {
        updateState { it.copy(isShowMenu = !it.isShowMenu) }
    }

    override fun handleSearchMode(isSearch: Boolean) {
        updateState { it.copy(isSearch = isSearch, isShowMenu = false, searchPosition = 0) }
    }

    override fun handleSearch(query: String?) {
        query ?: return

        val result = currentState.content.indexesOf(query)
            .map { it to it + query.length }

        updateState { it.copy(searchQuery = query, searchResults = result) }
    }

    override fun handleUpResult() {
        updateState { it.copy(searchPosition = it.searchPosition.dec()) }
    }

    override fun handleDownResult() {
        updateState { it.copy(searchPosition = it.searchPosition.inc()) }
    }

    override fun handleShare() {
        val msg = "Share is not implemented"
        notify(Notify.ErrorMessage(msg, "OK", null))
    }

    override fun handleBookmark() {
        val info = currentState.toArticlePersonalInfo()
        repository.updateArticlePersonalInfo(info.copy(isBookmark = !info.isBookmark))
        val msg = if (currentState.isBookmark) "Add to bookmarks" else "Remove from bookmarks"
        notify(Notify.TextMessage(msg))
    }

    override fun handleLike() {

        val toggleLike = {
            val info = currentState.toArticlePersonalInfo()
            repository.updateArticlePersonalInfo(info.copy(isLike = !info.isLike))
        }
        toggleLike()

        val msg = if (currentState.isLike) Notify.TextMessage("Mark is liked")
        else {
            Notify.ActionMessage(
                "Don`t like it anymore",
                "No, still like it",
                toggleLike
            )
        }
        notify(msg)
    }

    // load text from network
    override fun getArticleContent(): LiveData<String?> {
        return repository.loadArticleContent(articleId)
    }

    // load data from db
    override fun getArticleData(): LiveData<ArticleData?> {
        return repository.getArticle(articleId)
    }

    // load data from db
    override fun getArticlePersonalInfo(): LiveData<ArticlePersonalInfo?> {
        return repository.loadArticlePersonalInfo(articleId)
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
    val content: String = "Loading",                       // контент
    val reviews: List<Any> = emptyList()                   // комментарии
) : VMState {

    override fun toBundle(): Bundle {
        val map = copy(content = "Loading", isLoadingContent = true)
            .asMap()
            .toList()
            .toTypedArray()

        return bundleOf(*map)
    }

    override fun fromBundle(bundle: Bundle): VMState? {
        val map = bundle.keySet().associateWith { bundle[it] }
        return fromMap(map)
    }
}

data class BottombarData(
    val isLike: Boolean = false,                           // отмечено как понравившееся
    val isBookmark: Boolean = false,                       // добавлено в закладки
    val isShowMenu: Boolean = false,                       // меню отображается
    val isSearch: Boolean = false,                         // режим поиска
    val resultsCount: Int = 0,                             // количество найденных вхождений
    val searchPosition: Int = 0                            // текущая позиция поиска
)

data class SubmenuData(
    val isShowMenu: Boolean = false,                       // меню отображается
    val isBigText: Boolean = false,                        // шрифт увеличен
    val isDarkMode: Boolean = false                        // темный режим
)

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