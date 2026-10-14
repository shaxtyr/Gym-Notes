package com.example.gymnotes.domain.model

sealed class NotesDateGrid() {
    data class HeaderNotesDate(val monthYear: String) : NotesDateGrid()
    data class ContentNotesDate(val day: String, val dayOfWeek: String, val addedAt: String) : NotesDateGrid()
}

