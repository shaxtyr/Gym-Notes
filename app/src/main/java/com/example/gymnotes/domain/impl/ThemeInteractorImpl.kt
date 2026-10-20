package com.example.gymnotes.domain.impl

import com.example.gymnotes.domain.interactor.ThemeInteractor
import com.example.gymnotes.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeInteractorImpl(val themeRepository: ThemeRepository) : ThemeInteractor{
    override fun getIsDarkTheme(): Flow<Boolean> {
        return themeRepository.getIsDarkTheme()
    }

    override suspend fun setDarkTheme(isDarkMode: Boolean) {
        themeRepository.setDarkTheme(isDarkMode)
    }
}