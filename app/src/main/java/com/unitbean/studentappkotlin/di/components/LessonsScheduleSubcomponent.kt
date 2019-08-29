package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.LessonsScheduleModule
import com.unitbean.studentappkotlin.di.scopes.LessonsScheduleScope
import com.unitbean.studentappkotlin.ui.lessonsSchedule.presentsers.LessonsSchedulePresenter
import dagger.Subcomponent

@LessonsScheduleScope
@Subcomponent(modules = [LessonsScheduleModule::class])
interface LessonsScheduleSubcomponent {
    fun inject(presenter: LessonsSchedulePresenter)
}