package com.unitbean.studentappkotlin.ui.lessonsSchedule.presentsers

import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.lessonsSchedule.interactors.LessonsScheduleInteractor
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.DateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.IDateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.ILessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.LessonViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.views.LessonsScheduleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


@InjectViewState
class LessonsSchedulePresenter : MvpPresenter<LessonsScheduleView>(), CoroutineScope {

    @Inject
    lateinit var interactor: LessonsScheduleInteractor

    val dates = ArrayList<IDateViewModel>()
    val months = ArrayList<IDateViewModel>()
    var lessons = ArrayList<ILessonViewModel>()
    var thisDate: Date? = null

    override val coroutineContext: CoroutineContext = Dispatchers.Main


    init {
        DIManager.getLessonsScheduleSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearLessonsScheduleSubcomponent()
    }

    fun getMonthsFromDates(dates: ArrayList<IDateViewModel>){

    }

    fun loadDays() {
        launch {
            try {
                val newDates = withContext(Dispatchers.IO) {interactor.loadDaysBetweenDates()} ?: throw IllegalStateException("Dates not loaded")
                dates.clear()
                dates.addAll(newDates)
                val firstDate = Calendar.getInstance()
                firstDate.time = (dates[0] as DateViewModel).date
                val lastDate = Calendar.getInstance()
                lastDate.time = (dates[dates.size-1] as DateViewModel).date
                if (!interactor.isCurrentDateBetweenDates(firstDate, lastDate, thisDate)) {
                    dates.clear()
                    viewState.onGettingEmptyDates()
                    return@launch
                }
                val position = interactor.getCurrentDatePosition(firstDate, thisDate)
                val newMonths = interactor.getMonthsFromDates(dates)
                months.clear()
                months.addAll(newMonths)
                viewState.onDatesLoaded(dates, months, position)
            } catch (e: Exception) {
                dates.clear()
                viewState.onGettingEmptyDates()
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }

    fun firstDayOfMonthPosition(date: Date): Int{
        val firstDate = Calendar.getInstance()
        firstDate.time = (dates[0] as DateViewModel).date
        val position = interactor.getCurrentDatePosition(firstDate, date)
        return position
    }

    fun updateLessons(lessons: ArrayList<ILessonViewModel>){
        this.lessons.clear()
        this.lessons.addAll(lessons)
    }

    fun loadLessons(date: Date? = null) {
        launch {
            try {
                if (date != null) {
                    thisDate = date
                }
                val newLessons = withContext(Dispatchers.IO) {interactor.loadLessons(thisDate!!)} ?: throw IllegalStateException("Dates not loaded")
                val lessons = ArrayList<ILessonViewModel>()
                lessons.addAll(newLessons)
                viewState.onLessonsLoaded(lessons)
            } catch (e: Exception) {
                dates.clear()
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }

    fun deleteLesson(lesson: LessonViewModel) {
        launch {
            try {
                interactor.deleteLesson(lesson, thisDate!!)
                viewState.onLessonDeleted()
            } catch (e: Exception) {
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }

    fun setCurrentDate(date: Date) {
        launch {
            try {
                interactor.setCurrentDate(date)
            } catch (e: Exception) {
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }

    fun setPeriod(boolean: Boolean) {
        launch {
            try {
                interactor.setPeriod(boolean)
            } catch (e: Exception) {
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }
}