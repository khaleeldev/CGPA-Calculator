package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cgpa_user_prefs", Context.MODE_PRIVATE)

    private val _studentName = MutableStateFlow(prefs.getString("KEY_STUDENT_NAME", "") ?: "")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _academicYear = MutableStateFlow(prefs.getString("KEY_ACADEMIC_YEAR", "") ?: "")
    val academicYear: StateFlow<String> = _academicYear.asStateFlow()

    private val _gradingScale = MutableStateFlow(prefs.getFloat("KEY_GRADING_SCALE", 5.0f).toDouble())
    val gradingScale: StateFlow<Double> = _gradingScale.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("KEY_DARK_MODE", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun updateProfile(name: String, year: String) {
        _studentName.value = name
        _academicYear.value = year
        prefs.edit()
            .putString("KEY_STUDENT_NAME", name)
            .putString("KEY_ACADEMIC_YEAR", year)
            .apply()
    }

    fun updateGradingScale(scale: Double) {
        _gradingScale.value = scale
        prefs.edit().putFloat("KEY_GRADING_SCALE", scale.toFloat()).apply()
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.edit().putBoolean("KEY_DARK_MODE", enabled).apply()
    }
}
