package com.example.shared.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ThemeManager(private val dataStore: DataStore<Preferences>) {
    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

    val isDarkMode: Flow<Boolean?> = dataStore.data.map { it[IS_DARK_MODE] }

    suspend fun setDarkMode(isDark: Boolean) {
        dataStore.edit { it[IS_DARK_MODE] = isDark }
    }
}