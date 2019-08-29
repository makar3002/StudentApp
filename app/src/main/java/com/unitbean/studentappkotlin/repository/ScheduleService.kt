package com.unitbean.studentappkotlin.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.DateViewModel
import com.unitbean.studentappkotlin.utils.model.DatesModel
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.requests.DatesRequest
import com.unitbean.studentappkotlin.utils.requests.LessonRequest
import com.unitbean.studentappkotlin.utils.responses.AuthResponse
import com.unitbean.studentappkotlin.utils.responses.DatesResponse
import com.unitbean.studentappkotlin.utils.responses.LessonResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleService(private val prefs: SharedPreferences,
                  private val editor: SharedPreferences.Editor) {
    var startDate: Date? = null
    var endDate: Date? = null

    fun forceUpdateDates (firebaseApi: ApiService, user: UserModel): DatesModel? {
        val dates = firebaseApi.getDates(
            DatesRequest(
                user.institute,
                user.course,
                user.group,
                user.semester
            )
        )
        startDate = dates?.startDate ?: return null
        endDate = dates.endDate

        return dates.toDatesModel()
    }

    fun registerLesson (firebaseApi: ApiService, user: UserModel, lesson: LessonModel) {
        firebaseApi.registerLesson(
            LessonRequest(
                user.institute,
                user.course,
                user.group,
                user.semester,
                lesson.dayOfTheWeek,
                lesson.date,
                lesson.lessonName,
                lesson.lessonTeacherName,
                lesson.auditoryNumber,
                lesson.lessonStartTime,
                lesson.lessonEndTime
            )
        )
    }

    fun deleteLesson (firebaseApi: ApiService, user: UserModel, lesson: LessonModel) {
        firebaseApi.deleteLesson(
            LessonRequest(
                user.institute,
                user.course,
                user.group,
                user.semester,
                lesson.dayOfTheWeek,
                lesson.date,
                "",
                "",
                "",
                lesson.lessonStartTime,
                lesson.lessonEndTime
            )
        )
    }

    fun restoreDates(): DatesModel? {
        val dates = Gson().fromJson(prefs.getString(SAVED_DATES, ""), DatesModel::class.java)
        this.startDate = dates?.startDate
        this.endDate = dates?.endDate
        return dates
    }

    fun getLessons(firebaseApi: ApiService, user: UserModel, date: Date): ArrayList<LessonResponse>{
        return firebaseApi.getLessons(
            LessonRequest(
                user.institute,
                user.course,
                user.group,
                user.semester,
                when (SimpleDateFormat("EEEE", Locale.getDefault()).format(date)) {
                    "понедельник" -> "Понедельник"
                    "вторник" -> "Вторник"
                    "среда" -> "Среда"
                    "четверг" -> "Четверг"
                    "пятница" -> "Пятница"
                    "суббота" -> "Суббота"
                    "воскресенье" -> "Воскресенье"
                    else -> "ХЗ"
                },
                date,
                "",
                "",
                "",
                "",
                ""
            )
        )
    }

    fun setCurrentDate(
        date: Date
    ) {
        editor.putString(SAVED_CURRENT_DATE, Gson().toJson(date)).commit()
    }

    fun getCurrentDate(): Date {
        val date = Gson().fromJson(prefs.getString(SAVED_CURRENT_DATE, Date(0L).toString()), Date::class.java)
        return date
    }

    fun setPeriod(
        boolean: Boolean
    ) {
        editor.putString(SAVED_PERIOD, Gson().toJson(boolean)).commit()
    }

    fun getPeriod(): Boolean {
        val period = Gson().fromJson(prefs.getString(SAVED_PERIOD, Date(0L).toString()), Boolean::class.java)
        return period
    }

    fun setDates(
        firebaseApi: ApiService,
        user: UserModel,
        startDate: Date,
        endDate: Date
        ) {

    }

    private fun DatesResponse.toDatesModel(): DatesModel {
        return DatesModel(
            startDate,
            endDate
        )
    }

    companion object {
        private const val SAVED_DATES = "saved_dates"

        private const val SAVED_PERIOD = "saved_period"
        private const val SAVED_CURRENT_DATE = "saved_current_date"
    }
}