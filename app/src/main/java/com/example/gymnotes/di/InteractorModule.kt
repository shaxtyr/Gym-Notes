package com.example.gymnotes.di

import com.example.gymnotes.domain.impl.GymnasticInteractorImpl
import com.example.gymnotes.domain.impl.NoteInteractorImpl
import com.example.gymnotes.domain.impl.ThemeInteractorImpl
import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.interactor.ThemeInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory<NoteInteractor> { NoteInteractorImpl(get()) }
    factory<GymnasticInteractor> { GymnasticInteractorImpl(get()) }
    factory<ThemeInteractor> { ThemeInteractorImpl(get()) }
}
