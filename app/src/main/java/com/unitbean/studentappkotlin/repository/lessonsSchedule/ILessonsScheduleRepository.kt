package com.unitbean.studentappkotlin.repository.lessonsSchedule

import com.unitbean.studentappkotlin.utils.model.DatesModel
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.responses.LessonResponse
import java.util.*
import kotlin.collections.ArrayList

interface ILessonsScheduleRepository {
    fun isDatesLoaded(): Boolean
    fun getDates(): DatesModel?
    fun getUser(): UserModel?
    suspend fun updateUser(user: UserTokenModel): UserModel?
    fun getToken(): UserTokenModel?
    fun restoreDates(): DatesModel?
    suspend fun updateDates(user: UserModel): DatesModel?
    fun isUserLoaded(): Boolean
    fun loadLessons(user: UserModel, date: Date): ArrayList<LessonResponse>
    fun setCurrentDate(date: Date)
    fun setPeriod(boolean: Boolean)
    fun deleteLesson(lesson: LessonModel, user: UserModel)
}