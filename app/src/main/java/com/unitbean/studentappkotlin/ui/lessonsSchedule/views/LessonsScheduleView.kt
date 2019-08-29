package com.unitbean.studentappkotlin.ui.lessonsSchedule.views

import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.IDateViewModel
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.ILessonViewModel
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType


@StateStrategyType(OneExecutionStateStrategy::class)
interface LessonsScheduleView : MvpView{
    fun onDatesLoaded(dates: List<IDateViewModel>, months: List<IDateViewModel>, positions: Int? = null)
    fun onGettingEmptyDates()
    fun onLessonsLoaded(lessons: ArrayList<ILessonViewModel>)
    fun onLessonDeleted()
}