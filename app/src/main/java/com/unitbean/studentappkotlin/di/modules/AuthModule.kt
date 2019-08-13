package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.di.scopes.AuthScope
import com.unitbean.studentappkotlin.ui.auth.interactors.AuthInteractor
import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.unitbean.studentappkotlin.utils.repository.auth.AuthRepository
import com.unitbean.studentappkotlin.utils.repository.auth.IAuthRepository
import dagger.Module
import dagger.Provides

@Module
class AuthModule {
    @AuthScope
    @Provides
    fun provideRepository(apiService: ApiService, userService: UserService): IAuthRepository {
        return AuthRepository(apiService, userService)
    }

    @AuthScope
    @Provides
    fun provideInteractor(repository: IAuthRepository): AuthInteractor {
        return AuthInteractor(repository)
    }
}