package com.arturo.ru.skillbranch.skillarticles.viewmodels

import androidx.annotation.UiThread
import androidx.lifecycle.*
import java.lang.IllegalArgumentException

abstract class BaseViewModel<T>(initState: T) : ViewModel() {

    protected val state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    private val notifications = MutableLiveData<Event<Notification>>()

    protected val currentState: T
        get() = state.value!!

    @UiThread
    protected inline fun updateState(update: (currentState: T) -> T) {
        val updatedState: T = update(currentState)
        state.value = updatedState
    }

    @UiThread
    protected fun notify(content: Notification) {
        notifications.value = Event(content)
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        state.observe(owner, { onChanged(it) })
    }

    fun observeNotifications(
        owner: LifecycleOwner,
        onNotify: (notification: Notification) -> Unit
    ) {
        notifications.observe(owner, EventObserver { onNotify(it) })
    }

    protected fun <S> subscribeOnDataSource(
        source: LiveData<S>,
        onChanged: (newValue: S, currentState: T) -> T?
    ) {
        state.addSource(source) {
            state.value = onChanged(it, currentState) ?: return@addSource
        }
    }
}

class ViewModelFactory(private val params: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(params) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class Event<out E>(private val content: E) {

    var hasBeenHandled = false

    fun getContentIfNotHandled(): E? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }
}

class EventObserver<E>(private val onEventUnhandledContent: (E) -> Unit) : Observer<Event<E>> {

    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

sealed class Notification(val message: String) {

    data class TextMessage(val msg: String) : Notification(msg)

    data class ActionMessage(
        val msg: String,
        val actionLabel: String,
        val actionHandler: () -> Unit
    ) : Notification(msg)

    data class ErrorMessage(
        val msg: String,
        val errorLabel: String,
        val errorHandler: (() -> Unit)?
    ) : Notification(msg)
}