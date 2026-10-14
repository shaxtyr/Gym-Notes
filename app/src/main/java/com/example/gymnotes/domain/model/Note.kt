package com.example.gymnotes.domain.model

data class Note(
    val id: String,         // id
    val sets: String,       // подходы
    val reps: String,       // повторы
    val heft: String,       // вес
    val gymnasticName: String,   // упражнение
    val category: String,    // категория упражнения
    val addedAt: String,    // полная дата
    val monthYear: String,   // месяц-год для ui
    val day: String,          // число для ui
    val dayOfWeek: String     // день недели для ui
)
