package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.di.scopes.LessonsScheduleScope
import com.unitbean.studentappkotlin.repository.lessonsSchedule.ILessonsScheduleRepository
import com.unitbean.studentappkotlin.repository.lessonsSchedule.LessonsScheduleRepository
import com.unitbean.studentappkotlin.ui.lessonsSchedule.interactors.LessonsScheduleInteractor
import com.unitbean.studentappkotlin.repository.ApiService
import com.unitbean.studentappkotlin.repository.UserService
import dagger.Module
import dagger.Provides

@Module
class LessonsScheduleModule {
    @LessonsScheduleScope
    @Provides
    fun provideRepository(apiService: ApiService, userService: UserService): ILessonsScheduleRepository {
        return LessonsScheduleRepository(apiService, userService)
    }

    @LessonsScheduleScope
    @Provides
    fun provideInteractor(repository: ILessonsScheduleRepository): LessonsScheduleInteractor {
        return LessonsScheduleInteractor(repository)
    }
}