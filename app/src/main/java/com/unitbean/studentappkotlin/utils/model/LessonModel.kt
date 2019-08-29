package com.unitbean.studentappkotlin.utils.model

import java.util.*

data class LessonModel(
    val lessonName: String,
    val lessonTeacherName: String,
    val auditoryNumber: String,
    val lessonStartTime: String,
    val lessonEndTime: String,
    val dayOfTheWeek: String,
    val date: Date
)