package com.example.gymnotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymnotes.data.db.entity.GymnasticEntity

@Dao
interface GymnasticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GymnasticEntity)

    @Query("SELECT * FROM gymnastic_table WHERE category = :category")
    suspend fun getGymnasticsByCategory(category: String): List<GymnasticEntity>

    @Query("DELETE FROM gymnastic_table WHERE id = :id")
    suspend fun remove(id: String)
}