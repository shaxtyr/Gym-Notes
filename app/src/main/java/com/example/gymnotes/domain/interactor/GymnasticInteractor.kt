package com.example.gymnotes.domain.interactor

import com.example.gymnotes.domain.model.Gymnastic
import kotlinx.coroutines.flow.Flow

interface GymnasticInteractor {
    suspend fun removeGymnastic(id: String)
    suspend fun addGymnastic(category: String, gymnasticName: String)
    suspend fun getGymnasticsByCategory(category: String): Flow<List<Gymnastic>>
}