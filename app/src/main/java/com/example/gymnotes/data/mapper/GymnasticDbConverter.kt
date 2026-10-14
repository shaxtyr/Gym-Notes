package com.example.gymnotes.data.mapper

import com.example.gymnotes.data.db.entity.GymnasticEntity
import com.example.gymnotes.domain.model.Gymnastic

class GymnasticDbConverter {
    fun fromModelToEntity(gymnastic: Gymnastic): GymnasticEntity {
        return GymnasticEntity(
            id = gymnastic.id,
            name = gymnastic.name,
            category = gymnastic.category
        )
    }
    fun fromEntityToModel(gymnasticEntity: GymnasticEntity): Gymnastic {
        return Gymnastic(
            id = gymnasticEntity.id,
            name = gymnasticEntity.name,
            category = gymnasticEntity.category
        )
    }
}
