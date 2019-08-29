package com.unitbean.studentappkotlin.utils.requests

import java.util.*

class LessonRequest (
    val institute: String,
    val course: String,
    val group: String,
    val semester: String,
    val dayOfWeek: String,
    val date: Date,
    val lessonName: String,
    val teacherName: String,
    val auditoryNumber: String,
    val startTime: String,
    val endTime: String
)