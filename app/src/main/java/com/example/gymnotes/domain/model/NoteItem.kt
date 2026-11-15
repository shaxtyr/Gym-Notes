package com.example.gymnotes.domain.model

sealed class NoteItem {
    data class HeaderNoteItem(val gymName: String, val category: String) : NoteItem()
    data class ContentNoteItem(val gymName: String, val category: String, val sets: String, val reps: String, val heft: String) : NoteItem()
}