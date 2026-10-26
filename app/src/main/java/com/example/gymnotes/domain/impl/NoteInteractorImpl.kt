package com.example.gymnotes.domain.impl

import com.example.gymnotes.domain.interactor.NoteInteractor
import com.example.gymnotes.domain.model.GymNameAndCategory
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteInteractorImpl(val noteRepository: NoteRepository) : NoteInteractor {
    override suspend fun removeNote(id: String) {
        noteRepository.removeNote(id)
    }

    override suspend fun removeNotesByDate(date: String) {
        noteRepository.removeNotesByDate(date)
    }

    override fun getNotesByDate(addedAt: String): Flow<List<Note>> {
        return noteRepository.getNotesByDate(addedAt)
    }

    override suspend fun addNote(reps: String, heft: String, gymnastic: Gymnastic) {
        noteRepository.addNote(reps, heft, gymnastic)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }

    override suspend fun getNoteById(id: String): Note {
        return noteRepository.getNoteById(id)
    }

    override suspend fun getNotesByCategory(category: String): Flow<List<Note>> {
        return noteRepository.getNotesByCategory(category)
    }

    override suspend fun getGymNamesFromNotes(): List<GymNameAndCategory> {
        return noteRepository.getGymNamesFromNotes()
    }
}