package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CalculationEntity
import com.example.data.model.CourseItem
import com.example.data.repository.CgpaRepository
import com.example.data.repository.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CgpaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val prefsManager = UserPreferencesManager(application)
    val repository = CgpaRepository(db.calculationDao(), prefsManager)

    // User preferences state
    val studentName: StateFlow<String> = prefsManager.studentName
    val academicYear: StateFlow<String> = prefsManager.academicYear
    val gradingScale: StateFlow<Double> = prefsManager.gradingScale
    val isDarkMode: StateFlow<Boolean> = prefsManager.isDarkMode

    // Calculator state
    private val _courses = MutableStateFlow<List<CourseItem>>(
        listOf(
            CourseItem(name = "", units = 3, grade = ""),
            CourseItem(name = "", units = 3, grade = ""),
            CourseItem(name = "", units = 3, grade = "")
        )
    )
    val courses: StateFlow<List<CourseItem>> = _courses.asStateFlow()

    private val _currentTitle = MutableStateFlow("Current Semester")
    val currentTitle: StateFlow<String> = _currentTitle.asStateFlow()

    private val _calculatedCgpa = MutableStateFlow(0.0)
    val calculatedCgpa: StateFlow<Double> = _calculatedCgpa.asStateFlow()
    val currentGpa: StateFlow<Double> = _calculatedCgpa.asStateFlow()

    private val _totalUnits = MutableStateFlow(0)
    val totalUnits: StateFlow<Int> = _totalUnits.asStateFlow()

    // History state
    val historyItems: StateFlow<List<CalculationEntity>> = repository.allCalculations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cumulativeCgpa: StateFlow<Double> = combine(historyItems, _calculatedCgpa, _totalUnits) { items, curGpa, curUnits ->
        if (items.isNotEmpty()) {
            var totalPoints = 0.0
            var totalUnitsCount = 0

            items.forEach { item ->
                val u = if (item.totalUnits > 0) item.totalUnits else (item.courseCount * 3)
                totalPoints += (item.cgpa * u)
                totalUnitsCount += u
            }

            if (totalUnitsCount > 0) {
                Math.round((totalPoints / totalUnitsCount) * 100.0) / 100.0
            } else {
                val avg = items.map { it.cgpa }.average()
                Math.round(avg * 100.0) / 100.0
            }
        } else {
            curGpa
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val overallAverage: StateFlow<Double> = cumulativeCgpa

    val calculationsCount: StateFlow<Int> = historyItems.combine(historyItems) { items, _ ->
        items.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun addCourseRow() {
        val currentList = _courses.value.toMutableList()
        currentList.add(CourseItem(name = "", units = 3, grade = ""))
        _courses.value = currentList
        recalculate()
    }

    fun removeCourseRow(index: Int) {
        val currentList = _courses.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _courses.value = currentList
            recalculate()
        }
    }

    fun updateCourseName(index: Int, name: String) {
        val currentList = _courses.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(name = name.uppercase())
            _courses.value = currentList
        }
    }

    fun updateCourseUnits(index: Int, units: Int) {
        val currentList = _courses.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(units = units)
            _courses.value = currentList
            recalculate()
        }
    }

    fun updateCourseGrade(index: Int, grade: String) {
        val currentList = _courses.value.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(grade = grade)
            _courses.value = currentList
            recalculate()
        }
    }

    fun setSemesterTitle(title: String) {
        _currentTitle.value = title
    }

    fun recalculate() {
        val maxScale = prefsManager.gradingScale.value
        var sumUnits = 0
        var sumPoints = 0.0

        _courses.value.forEach { course ->
            if (course.grade.isNotBlank()) {
                val pts = getGradePoint(course.grade, maxScale)
                sumUnits += course.units
                sumPoints += (course.units * pts)
            }
        }

        val cgpa = if (sumUnits > 0) sumPoints / sumUnits else 0.0
        val roundedCgpa = Math.round(cgpa * 100.0) / 100.0
        _calculatedCgpa.value = roundedCgpa
        _totalUnits.value = sumUnits
    }

    private fun getGradePoint(grade: String, maxScale: Double): Double {
        return if (maxScale == 5.0) {
            when (grade.uppercase()) {
                "A" -> 5.0
                "B" -> 4.0
                "C" -> 3.0
                "D" -> 2.0
                "E" -> 1.0
                "F" -> 0.0
                else -> 0.0
            }
        } else {
            // 4.0 scale
            when (grade.uppercase()) {
                "A" -> 4.0
                "B" -> 3.0
                "C" -> 2.0
                "D" -> 1.0
                "E" -> 0.5
                "F" -> 0.0
                else -> 0.0
            }
        }
    }

    fun saveResults(title: String) {
        recalculate()
        viewModelScope.launch {
            repository.saveCalculation(
                title = if (title.isBlank()) "Semester Calculation" else title,
                cgpa = _calculatedCgpa.value,
                totalUnits = _totalUnits.value,
                courseCount = _courses.value.size,
                maxScale = prefsManager.gradingScale.value,
                courses = _courses.value
            )
            resetCalculator()
        }
    }

    fun resetCalculator() {
        _courses.value = listOf(
            CourseItem(name = "", units = 3, grade = ""),
            CourseItem(name = "", units = 3, grade = ""),
            CourseItem(name = "", units = 3, grade = "")
        )
        _currentTitle.value = "Current Semester"
        _calculatedCgpa.value = 0.0
        _totalUnits.value = 0
    }

    fun loadPastResult(entity: CalculationEntity) {
        val loadedCourses = repository.parseCourses(entity.coursesJson)
        if (loadedCourses.isNotEmpty()) {
            _courses.value = loadedCourses
        }
        _currentTitle.value = entity.title
        recalculate()
    }

    fun deleteHistoryItem(id: Int) {
        viewModelScope.launch {
            repository.deleteCalculation(id)
        }
    }

    fun updateProfile(name: String, year: String) {
        prefsManager.updateProfile(name, year)
    }

    fun updateGradingScale(scale: Double) {
        prefsManager.updateGradingScale(scale)
        recalculate()
    }

    fun toggleDarkMode(enabled: Boolean) {
        prefsManager.setDarkMode(enabled)
    }
}
