package ru.skillbranch.skillarticles.data.delegates

import androidx.datastore.preferences.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.skillbranch.skillarticles.data.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T, private val customKey: String? = null) {

    operator fun provideDelegate(
        thisRef: PrefManager,
        prop: KProperty<*>
    ): ReadWriteProperty<PrefManager, T> {

        val key = createKey(customKey ?: prop.name, defaultValue)
        return object : ReadWriteProperty<PrefManager, T> {

            private var storedValue: T? = null

            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T {
                if (storedValue == null) {
                    val flowValue = thisRef.dataStore.data
                        .map { prefs -> prefs[key] ?: defaultValue }
                    // Dispatchers.IO is not required since dataStore already performs read/write operations on IO
                    storedValue = runBlocking(Dispatchers.IO) { flowValue.first() }
                }
                return storedValue!!
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T) {
                storedValue = value
                thisRef.scope.launch {
                    thisRef.dataStore.edit { pref ->
                        pref[key] = value
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked_cast")
    fun createKey(name: String, value: T): Preferences.Key<T> =
        when (value) {
            is Int -> intPreferencesKey(name)
            is Long -> longPreferencesKey(name)
            is Double -> doublePreferencesKey(name)
            is Float -> floatPreferencesKey(name)
            is String -> stringPreferencesKey(name)
            is Boolean -> booleanPreferencesKey(name)
            else -> error("This type can not be stored into Preferences")
        }.run { this as Preferences.Key<T> }
}