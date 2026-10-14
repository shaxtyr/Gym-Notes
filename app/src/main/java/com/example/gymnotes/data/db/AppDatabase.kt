package com.example.gymnotes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymnotes.data.db.dao.GymnasticDao
import com.example.gymnotes.data.db.dao.NoteDao
import com.example.gymnotes.data.db.entity.NoteEntity
import com.example.gymnotes.data.db.entity.GymnasticEntity

@Database(
    entities = [NoteEntity::class, GymnasticEntity::class],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun gymnasticDao(): GymnasticDao
}
