package com.example.data.repository

import com.example.data.local.CalculationDao
import com.example.data.local.CalculationEntity
import com.example.data.model.CourseItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

class CgpaRepository(
    private val calculationDao: CalculationDao,
    val userPreferencesManager: UserPreferencesManager
) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val courseListType = Types.newParameterizedType(List::class.java, CourseItem::class.java)
    private val courseListAdapter = moshi.adapter<List<CourseItem>>(courseListType)

    val allCalculations: Flow<List<CalculationEntity>> = calculationDao.getAllCalculations()

    suspend fun saveCalculation(
        title: String,
        cgpa: Double,
        totalUnits: Int,
        courseCount: Int,
        maxScale: Double,
        courses: List<CourseItem>,
        id: Int = 0
    ) {
        val coursesJson = courseListAdapter.toJson(courses)
        val entity = CalculationEntity(
            id = id,
            title = title,
            cgpa = cgpa,
            totalUnits = totalUnits,
            courseCount = courseCount,
            maxScale = maxScale,
            timestamp = System.currentTimeMillis(),
            coursesJson = coursesJson
        )
        calculationDao.insertCalculation(entity)
    }

    suspend fun deleteCalculation(id: Int) {
        calculationDao.deleteCalculationById(id)
    }

    fun parseCourses(json: String): List<CourseItem> {
        return try {
            courseListAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
