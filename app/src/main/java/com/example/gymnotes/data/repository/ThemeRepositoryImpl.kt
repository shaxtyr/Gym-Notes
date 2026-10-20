package com.example.gymnotes.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymnotes.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(private val context: Context) : ThemeRepository {
    private val Context.themeDataStore by preferencesDataStore("user_theme_setting")

    override fun getIsDarkTheme(): Flow<Boolean> = context.themeDataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: false
    }

    override suspend fun setDarkTheme(isDarkMode: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    companion object {
        private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }
}