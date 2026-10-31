package com.example.gymnotes.data.repository

import android.util.Log
import com.example.gymnotes.data.db.AppDatabase
import com.example.gymnotes.data.db.entity.GymnasticEntity
import com.example.gymnotes.data.mapper.GymnasticDbConverter
import com.example.gymnotes.domain.model.Gymnastic
import com.example.gymnotes.domain.repository.GymnasticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class GymnasticRepositoryImpl(
    private val database: AppDatabase,
    private val gymnasticDbConverter: GymnasticDbConverter
) : GymnasticRepository {
    override suspend fun removeGymnastic(id: String) {
        database.gymnasticDao().remove(id)
    }

    override suspend fun addGymnastic(category: String, gymnasticName: String) {
        val gymnasticId = UUID.randomUUID().toString()
        database.gymnasticDao().insert(GymnasticEntity(
            id = gymnasticId,
            name = gymnasticName,
            category = category
        ))
    }

    override suspend fun renameGymnastic(id: String, newGymnasticName: String) {
        database.gymnasticDao().updateGymnasticName(id, newGymnasticName)
    }

    override fun getGymnasticsByCategory(category: String): Flow<List<Gymnastic>> = flow {
        val gymnasticsByCategory = database.gymnasticDao().getGymnasticsByCategory(category)
        emit(gymnasticsByCategory.map { gymnasticEntity -> gymnasticDbConverter.fromEntityToModel(gymnasticEntity) })
    }

    override fun getAllGymnastics(): Flow<List<Gymnastic>> =
        database.gymnasticDao().getAllGymnastics().map {
            listGymnasticEntity -> listGymnasticEntity.map {
                gymnastic -> gymnasticDbConverter.fromEntityToModel(gymnastic)
            }
        }
}
