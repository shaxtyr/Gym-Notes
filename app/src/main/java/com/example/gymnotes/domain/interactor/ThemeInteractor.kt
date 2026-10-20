package com.example.gymnotes.domain.interactor

import kotlinx.coroutines.flow.Flow

interface ThemeInteractor {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(isDarkMode: Boolean)
}