package com.example.gymnotes.domain.interactor

import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteInteractor {
    suspend fun removeNote(id: String)
    suspend fun removeNotesByDate(date: String)
    fun getNotesByDate(addedAt: String): Flow<List<Note>>
    suspend fun addNote(reps: String, heft: String, gymnastic: Gymnastic)
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note
    suspend fun getNotesByCategory(category: String): Flow<List<Note>>
    suspend fun getGymNamesFromNotes(): List<String>
}