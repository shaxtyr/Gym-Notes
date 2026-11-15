package com.example.gymnotes.domain.model

sealed class NotesDateGridItem {
    data class HeaderNotesDateItem(val monthYear: String) : NotesDateGridItem()
    data class ContentNotesDateItem(val day: String, val dayOfWeek: String, val addedAt: String, val categories: List<String>) : NotesDateGridItem()
}

