package com.example.gymnotes.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(isDarkMode: Boolean)
}