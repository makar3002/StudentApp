package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.di.scopes.AddLessonScope
import com.unitbean.studentappkotlin.di.scopes.AuthScope
import com.unitbean.studentappkotlin.ui.auth.interactors.AuthInteractor
import com.unitbean.studentappkotlin.repository.ApiService
import com.unitbean.studentappkotlin.repository.ScheduleService
import com.unitbean.studentappkotlin.repository.UserService
import com.unitbean.studentappkotlin.repository.auth.AddLessonRepository
import com.unitbean.studentappkotlin.repository.auth.AuthRepository
import com.unitbean.studentappkotlin.repository.auth.IAddLessonRepository
import com.unitbean.studentappkotlin.repository.auth.IAuthRepository
import com.unitbean.studentappkotlin.ui.auth.interactors.AddLessonInteractor
import dagger.Module
import dagger.Provides

@Module
class AddLessonModule {
    @AddLessonScope
    @Provides
    fun provideRepository(apiService: ApiService, userService: UserService, scheduleService: ScheduleService): IAddLessonRepository {
        return AddLessonRepository(apiService, userService, scheduleService)
    }

    @AddLessonScope
    @Provides
    fun provideInteractor(repository: IAddLessonRepository): AddLessonInteractor {
        return AddLessonInteractor(repository)
    }
}