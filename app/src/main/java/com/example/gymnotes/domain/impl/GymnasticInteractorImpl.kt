package com.example.gymnotes.domain.impl

import com.example.gymnotes.domain.interactor.GymnasticInteractor
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.repository.GymnasticRepository
import kotlinx.coroutines.flow.Flow

class GymnasticInteractorImpl(val gymnasticRepository: GymnasticRepository) : GymnasticInteractor {
    override suspend fun removeGymnastic(id: String) {
        gymnasticRepository.removeGymnastic(id)
    }

    override suspend fun addGymnastic(category: String, gymnasticName: String) {
        gymnasticRepository.addGymnastic(category, gymnasticName)
    }

    override suspend fun getGymnasticsByCategory(category: String): Flow<List<Gymnastic>> {
        return gymnasticRepository.getGymnasticsByCategory(category)
    }
}