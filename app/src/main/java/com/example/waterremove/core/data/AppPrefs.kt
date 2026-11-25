package com.example.waterremove.core.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "app_prefs")

@Singleton
class AppPrefs @Inject constructor(@param:ApplicationContext private val context: Context) {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val OPEN_COUNT = intPreferencesKey("open_count")
        val RATED = booleanPreferencesKey("rated")
        val RATE_SHOWN = booleanPreferencesKey("rate_shown")
        fun tutorialSeen(feature: String) = booleanPreferencesKey("tutorial_seen_$feature")
    }
    private suspend fun prefs() = context.dataStore.data.first()
    suspend fun isOnboardingDone(): Boolean = prefs()[Keys.ONBOARDING_DONE] ?: false

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }
    suspend fun increaseOpenCount(): Int {
        var newCount = 0
        context.dataStore.edit { p ->
            newCount = (p[Keys.OPEN_COUNT] ?: 0) + 1
            p[Keys.OPEN_COUNT] = newCount
        }
        return newCount
    }
    suspend fun openCount(): Int = prefs()[Keys.OPEN_COUNT] ?: 0

    suspend fun isRated(): Boolean = prefs()[Keys.RATED] ?: false

    suspend fun setRated() {
        context.dataStore.edit { it[Keys.RATED] = true }
    }

    suspend fun isRateShown(): Boolean = prefs()[Keys.RATE_SHOWN] ?: false

    suspend fun setRateShown() {
        context.dataStore.edit { it[Keys.RATE_SHOWN] = true }
    }

    suspend fun isTutorialSeen(feature: String): Boolean = prefs()[Keys.tutorialSeen(feature)] ?: false

    suspend fun setTutorialSeen(feature: String) {
        context.dataStore.edit { it[Keys.tutorialSeen(feature)] = true }
    }
}
