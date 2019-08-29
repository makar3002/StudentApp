package com.unitbean.studentappkotlin.repository.lessonsSchedule

import com.unitbean.studentappkotlin.repository.ApiService
import com.unitbean.studentappkotlin.repository.ScheduleService
import com.unitbean.studentappkotlin.repository.UserService
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.LessonViewModel
import com.unitbean.studentappkotlin.utils.model.DatesModel
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.responses.LessonResponse
import java.util.*
import kotlin.collections.ArrayList

class LessonsScheduleRepository (private val apiService: ApiService,
                                 private val userService: UserService,
                                 private val scheduleService: ScheduleService
) : ILessonsScheduleRepository {
    override fun loadLessons(user: UserModel, date: Date): ArrayList<LessonResponse> {
        return scheduleService.getLessons(apiService, user, date)
    }

    override fun getDates(): DatesModel? = DatesModel(scheduleService.startDate ?: Date(0L), scheduleService.endDate ?: Date(0L))

    override suspend fun updateUser(user: UserTokenModel): UserModel? {
        return userService.forceUpdateUser(apiService, user.userId, user.type)
    }

    override suspend fun updateDates(user: UserModel): DatesModel? {
        return scheduleService.forceUpdateDates(apiService, user)
    }

    override fun restoreDates(): DatesModel? {
        return scheduleService.restoreDates()
    }
    override fun isUserLoaded(): Boolean = userService.userToken != null && userService.user != null

    override fun isDatesLoaded(): Boolean = scheduleService.startDate != null && scheduleService.endDate != null

    override fun getToken(): UserTokenModel? = userService.restoreUserToken()

    override fun getUser(): UserModel? = userService.user

    override fun setCurrentDate(date: Date) {
        scheduleService.setCurrentDate(date)
    }

    override fun deleteLesson(lesson: LessonModel, user: UserModel) {
        scheduleService.deleteLesson(apiService, user, lesson)
    }

    override fun setPeriod(boolean: Boolean) {
        scheduleService.setPeriod(boolean)
    }

}