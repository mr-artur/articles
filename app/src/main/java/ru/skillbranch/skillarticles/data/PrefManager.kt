package ru.skillbranch.skillarticles.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import ru.skillbranch.skillarticles.App

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PrefManager(context: Context = App.applicationContext()) {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("PrefManager", "err ${throwable.message}")
    }
    val scope = CoroutineScope(SupervisorJob() + errorHandler)
    val dataStore = context.dataStore

    var isBigText by PrefDelegate(false)
    var isDarkMode by PrefDelegate(false)

    val settings: LiveData<AppSettings>
        get() {
            val isBigText = dataStore.data.map { prefs ->
                prefs[booleanPreferencesKey(this::isBigText.name)] ?: false
            }
            val isDarkMode = dataStore.data.map { prefs ->
                prefs[booleanPreferencesKey(this::isDarkMode.name)] ?: false
            }
            return isBigText.zip(isDarkMode) { bigText, darkMode ->
                AppSettings(
                    isDarkMode = darkMode,
                    isBigText = bigText
                )
            }
                .onEach { Log.d("PrefManager", "settings $it") }
                .distinctUntilChanged()
                .asLiveData()
        }
}