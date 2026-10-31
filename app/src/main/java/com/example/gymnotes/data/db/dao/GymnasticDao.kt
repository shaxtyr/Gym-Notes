package com.example.gymnotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymnotes.data.db.entity.GymnasticEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GymnasticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GymnasticEntity)

    @Query("SELECT * FROM gymnastic_table")
    fun getAllGymnastics(): Flow<List<GymnasticEntity>>

    @Query("SELECT * FROM gymnastic_table WHERE category = :category")
    suspend fun getGymnasticsByCategory(category: String): List<GymnasticEntity>

    @Query("UPDATE gymnastic_table SET name = :newName WHERE id = :id")
    suspend fun updateGymnasticName(id: String, newName: String)

    @Query("DELETE FROM gymnastic_table WHERE id = :id")
    suspend fun remove(id: String)
}