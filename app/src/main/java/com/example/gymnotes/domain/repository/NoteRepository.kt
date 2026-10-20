package com.example.gymnotes.domain.repository

import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun removeNote(id: String)
    suspend fun removeNotesByDate(date: String)
    fun getNotesByDate(addedAt: String): Flow<List<Note>>
    suspend fun addNote(reps: String, heft: String, gymnastic: Gymnastic)
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note
    fun getNotesByCategory(category: String): Flow<List<Note>>
}