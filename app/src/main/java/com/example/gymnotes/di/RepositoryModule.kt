package com.example.gymnotes.di

import com.example.gymnotes.data.repository.GymnasticRepositoryImpl
import com.example.gymnotes.data.repository.NoteRepositoryImpl
import com.example.gymnotes.domain.repository.GymnasticRepository
import com.example.gymnotes.domain.repository.NoteRepository
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
}