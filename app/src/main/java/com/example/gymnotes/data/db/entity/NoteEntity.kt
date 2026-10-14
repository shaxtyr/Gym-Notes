package com.example.gymnotes.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val sets: String,
    val reps: String,
    val heft: String,
    val gymnasticName: String,
    val category: String,
    val addedAt: String
)