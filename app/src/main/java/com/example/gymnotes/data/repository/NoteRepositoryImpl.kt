package com.example.gymnotes.data.repository

import com.example.gymnotes.data.db.AppDatabase
import com.example.gymnotes.data.db.entity.NoteEntity
import com.example.gymnotes.data.mapper.NoteDbConverter
import com.example.gymnotes.domain.model.GymNameAndCategory
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class NoteRepositoryImpl(
    private val database: AppDatabase,
    private val noteDbConverter: NoteDbConverter
) : NoteRepository {
    override suspend fun removeNote(id: String) {
        database.noteDao().deleteNote(id)
    }

    override suspend fun removeNotesByDate(date: String) {
        database.noteDao().deleteNotesByDate(date)
    }

    override fun getNotesByDate(addedAt: String): Flow<List<Note>> = flow {
        val noteByDate = database.noteDao().getNotesByDate(addedAt)//.sortedBy { it.category }
        emit(noteByDate.map { noteEntity -> noteDbConverter.fromEntityToModel(noteEntity) })
    }

    override suspend fun addNote(reps: String, heft: String, gymnastic: Gymnastic) {
        //val addedAtFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        val noteId = UUID.randomUUID().toString()
        val noteAddedAt = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        //val noteAddedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val notesByCategory = database.noteDao().getNotesByCategory(gymnastic.category).reversed() // инверсия для увеличения sets
        var sets = "1"
        for (item in notesByCategory) {
            if ((item.gymnasticName == gymnastic.name) && (item.addedAt == noteAddedAt)) {
                sets = (item.sets.toInt() + 1).toString()
                break
            }
        }
        database.noteDao().insert(NoteEntity(
            id = noteId,
            sets = sets,
            reps = reps,
            heft = heft,
            gymnasticName = gymnastic.name,
            category = gymnastic.category,
            addedAt = noteAddedAt,
        ))
    }

    override fun getAllNotes(): Flow<List<Note>> =
        database.noteDao().getAllNotes().map {
            listNoteEntity -> listNoteEntity.map {
                noteEntity -> noteDbConverter.fromEntityToModel(noteEntity)
            }
        }

    override suspend fun getNoteById(id: String): Note =
        noteDbConverter.fromEntityToModel(database.noteDao().getNoteById(id))

    override fun getNotesByCategory(category: String): Flow<List<Note>> = flow {
        val noteByCategory = database.noteDao().getNotesByCategory(category)
        emit(noteByCategory.map { noteEntity -> noteDbConverter.fromEntityToModel(noteEntity) })
    }

    override suspend fun getGymNamesFromNotes(): List<GymNameAndCategory> {
        return database.noteDao().getUniqueGymNamesFromNotes().sortedBy { it.category }
    }
}

/*(LocalDateTime.parse(item.addedAt).format(addedAtFormatter) == LocalDateTime.parse(noteAddedAt).format(addedAtFormatter))*/