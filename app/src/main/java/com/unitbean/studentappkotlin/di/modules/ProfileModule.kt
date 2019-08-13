package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.di.scopes.ProfileScope
import com.unitbean.studentappkotlin.ui.profile.interactors.ProfileInteractor
import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.unitbean.studentappkotlin.utils.repository.profile.IProfileRepository
import com.unitbean.studentappkotlin.utils.repository.profile.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class ProfileModule{
    @ProfileScope
    @Provides
    fun provideRepository(apiService: ApiService, userService: UserService): IProfileRepository {
        return ProfileRepository(apiService, userService)
    }

    @ProfileScope
    @Provides
    fun provideInteractor(repository: IProfileRepository): ProfileInteractor {
        return ProfileInteractor(repository)
    }
}
