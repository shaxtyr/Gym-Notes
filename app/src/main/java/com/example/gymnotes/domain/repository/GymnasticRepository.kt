package com.example.gymnotes.domain.repository

import com.example.gymnotes.domain.model.Gymnastic
import kotlinx.coroutines.flow.Flow

interface GymnasticRepository {
    suspend fun removeGymnastic(id: String)
    suspend fun addGymnastic(category: String, gymnasticName: String)
    suspend fun renameGymnastic(id: String, newGymnasticName: String)
    fun getGymnasticsByCategory(category: String): Flow<List<Gymnastic>>
}