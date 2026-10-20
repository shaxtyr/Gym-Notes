package com.example.gymnotes.di

import com.example.gymnotes.data.repository.GymnasticRepositoryImpl
import com.example.gymnotes.data.repository.NoteRepositoryImpl
import com.example.gymnotes.data.repository.ThemeRepositoryImpl
import com.example.gymnotes.domain.repository.GymnasticRepository
import com.example.gymnotes.domain.repository.NoteRepository
import com.example.gymnotes.domain.repository.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> { NoteRepositoryImpl(
        database = get(),
        noteDbConverter = get()
    ) }
    single<GymnasticRepository> { GymnasticRepositoryImpl(
        database = get(),
        gymnasticDbConverter = get()
    ) }
    single<ThemeRepository> { ThemeRepositoryImpl(
        context = get()
    ) }
}