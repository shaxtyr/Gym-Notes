package com.example.gymnotes.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "gymnastic_table")
data class GymnasticEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String
)
