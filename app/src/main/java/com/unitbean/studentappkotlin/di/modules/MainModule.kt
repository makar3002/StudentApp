package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.di.scopes.MainScope
import com.unitbean.studentappkotlin.ui.main.interactors.MainInteractor
import com.unitbean.studentappkotlin.repository.ApiService
import com.unitbean.studentappkotlin.repository.UserService
import com.unitbean.studentappkotlin.repository.main.IMainRepository
import com.unitbean.studentappkotlin.repository.main.MainRepository
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @MainScope
    @Provides
    fun provideRepository(apiService: ApiService, userService: UserService): IMainRepository {
        return MainRepository(apiService, userService)
    }

    @MainScope
    @Provides
    fun provideInteractor(repository: IMainRepository): MainInteractor {
        return MainInteractor(repository)
    }
}
