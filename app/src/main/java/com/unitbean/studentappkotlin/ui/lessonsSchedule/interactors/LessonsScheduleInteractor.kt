package com.unitbean.studentappkotlin.ui.lessonsSchedule.interactors

import com.unitbean.studentappkotlin.repository.lessonsSchedule.ILessonsScheduleRepository
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.*
import com.unitbean.studentappkotlin.utils.model.DatesModel
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LessonsScheduleInteractor (private val repository: ILessonsScheduleRepository) {


    private val dayOfTheWeekFormatter: SimpleDateFormat by lazy { SimpleDateFormat("EEEE", Locale.getDefault()) }

    suspend fun loadDaysBetweenDates(): MutableList<IDateViewModel>? {

        val dateList: ArrayList<Date> = ArrayList()

        val dates = loadDates() ?: return dateList.map {
            DateViewModel(it)
        }.toMutableList()

        var firstDate: Calendar = Calendar.getInstance()
        firstDate.time = dates.startDate

        val lastDate: Calendar = Calendar.getInstance()
        lastDate.time = dates.endDate

        while (!firstDate.after(lastDate)) {
            dateList.add(firstDate.time)
            firstDate.add(Calendar.DATE, 1)
        }


        val dateBetweenDates: MutableList<IDateViewModel> = dateList.map {
            DateViewModel(it)
        }.toMutableList()

        return dateBetweenDates
    }

    fun setCurrentDate (date: Date){
        repository.setCurrentDate(date)
    }

    fun setPeriod (boolean: Boolean){
        repository.setPeriod(boolean)
    }

    fun getMonthsFromDates(dates: ArrayList<IDateViewModel>): MutableList<IDateViewModel>{
        val monthsList: ArrayList<Date> = ArrayList()

        var firstDate: Calendar = Calendar.getInstance()
        firstDate.time = (dates[0] as DateViewModel).date
        firstDate.set(Calendar.DAY_OF_MONTH, 1)

        val lastDate: Calendar = Calendar.getInstance()
        lastDate.time = (dates[dates.size-1] as DateViewModel).date
        lastDate.set(Calendar.DAY_OF_MONTH, 1)

        while (!firstDate.after(lastDate)) {
            monthsList.add(firstDate.time)
            firstDate.add(Calendar.MONTH, 1)
        }

        return monthsList.map{
            DateViewModel(it)
        }.toMutableList()
    }

    suspend fun loadLessons(date: Date): MutableList<ILessonViewModel>? {
        val lessons: MutableList<ILessonViewModel> = repository.loadLessons(loadUser(), date).map {
            LessonViewModel(it)
        }.toMutableList()
        lessons.add(AddLessonViewModel("addLesson"))
        return lessons
    }

    suspend fun deleteLesson(lesson: LessonViewModel, date: Date) {
        repository.deleteLesson(
            LessonModel(
            "",
            "",
            "",
            lesson.startTime,
            lesson.endTime,
                when (dayOfTheWeekFormatter.format(date)){
                    "понедельник" -> "Понедельник"
                    "вторник" -> "Вторник"
                    "среда" -> "Среда"
                    "четверг" -> "Четверг"
                    "пятница" -> "Пятница"
                    "суббота" -> "Суббота"
                    "воскресенье" -> "Воскресенье"
                    else -> "ХЗ"
                },
            date
        ), repository.getUser()!!)
    }

    fun isCurrentDateBetweenDates (startDate: Calendar, endDate: Calendar, date: Date? = null): Boolean{
        val currentDate = Calendar.getInstance()
        if (date != null) currentDate.time = date
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        return (currentDate.after(startDate) && currentDate.before(endDate))
    }

    fun getCurrentDatePosition (startDate: Calendar, date: Date? = null): Int{
        var position = 0
        val currentDate = Calendar.getInstance()
        if (date != null) currentDate.time = date
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        while (!startDate.after(currentDate)){
            position++
            startDate.add(Calendar.DATE, 1)
        }
        return position
    }

    private suspend fun loadUser(): UserModel {
        return if (repository.isUserLoaded()) {
            repository.getUser() ?: throw IllegalStateException("User not loaded")
        } else {
            val user = repository.updateUser(repository.getToken() ?: throw IllegalStateException("Token not loaded"))
            user ?: throw IllegalStateException("User not loaded")
        }
    }

    suspend fun loadDates(): DatesModel? {
        return if (repository.isDatesLoaded()) {
            repository.getDates() ?: throw IllegalStateException("Dates not loaded")
        } else {
            var dates = repository.restoreDates()
            if (dates == null) {
                val user = loadUser()
                dates = repository.updateDates(user)
            }
            dates
        }
    }
}