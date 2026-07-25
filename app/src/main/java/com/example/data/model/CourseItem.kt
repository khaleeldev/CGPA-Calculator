package com.example.data.model

import java.util.UUID

data class CourseItem(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var units: Int = 3,
    var grade: String = "" // "A", "B", "C", "D", "E", "F" or empty
)
