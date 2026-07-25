package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val cgpa: Double,
    val totalUnits: Int,
    val courseCount: Int,
    val maxScale: Double = 5.0,
    val timestamp: Long = System.currentTimeMillis(),
    val coursesJson: String = "[]"
)
