package com.unitbean.studentappkotlin.repository.auth

import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import com.unitbean.studentappkotlin.utils.model.UserTokenModel
import java.util.*

interface IAddLessonRepository {
    /*fun logout()

    fun isUserLoaded(): Boolean
    fun getToken(): UserTokenModel?
    suspend fun updateUser(token: UserTokenModel): UserModel?*/
    fun getUser(): UserModel?
    suspend fun registerLesson(user: UserModel, lesson: LessonModel)
    fun getCurrentDate(): Date
    fun getPeriod(): Boolean
}