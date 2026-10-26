package com.example.gymnotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymnotes.data.db.entity.NoteEntity
import com.example.gymnotes.domain.model.GymNameAndCategory
import com.example.gymnotes.domain.model.Gymnastic
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(entity: NoteEntity)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity

    @Query("SELECT * FROM note_table WHERE category = :category")
    suspend fun getNotesByCategory(category: String): List<NoteEntity>

    @Query("SELECT * FROM note_table WHERE addedAt = :addedAt")
    suspend fun getNotesByDate(addedAt: String): List<NoteEntity>

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Query("DELETE FROM note_table WHERE addedAt = :date")
    suspend fun deleteNotesByDate(date: String)

    @Query("SELECT DISTINCT gymnasticName, category FROM note_table")
    suspend fun getUniqueGymNamesFromNotes(): List<GymNameAndCategory>

}