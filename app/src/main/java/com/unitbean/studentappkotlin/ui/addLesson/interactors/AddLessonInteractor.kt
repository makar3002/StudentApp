package com.unitbean.studentappkotlin.ui.auth.interactors

import com.unitbean.studentappkotlin.repository.auth.IAddLessonRepository
import com.unitbean.studentappkotlin.repository.auth.IAuthRepository
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import java.util.*

class AddLessonInteractor (private val repository: IAddLessonRepository) {

    suspend fun registerLesson(lesson: LessonModel){

        repository.registerLesson(repository.getUser()!!, lesson)
    }

    fun getCurrentDate(): Date = repository.getCurrentDate()
    fun getPeriod(): Boolean = repository.getPeriod()
}