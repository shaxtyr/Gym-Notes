package com.example.gymnotes.di

import com.example.gymnotes.presentation.notes.NotesDateViewModel
import com.example.gymnotes.presentation.notes.NotesViewModel
import com.example.gymnotes.presentation.working.GymViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NotesDateViewModel(get()) }
    viewModel { NotesViewModel(get()) }
    viewModel { GymViewModel(get(), get()) }
}