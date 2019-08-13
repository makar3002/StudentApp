package com.unitbean.studentappkotlin.di.modules

import com.unitbean.studentappkotlin.utils.repository.profileHost.IProfileHostRepository
import com.unitbean.studentappkotlin.di.scopes.ProfileHostScope
import com.unitbean.studentappkotlin.ui.profileHost.interactors.ProfileHostInteractor
import com.unitbean.studentappkotlin.utils.repository.UserService
import com.unitbean.studentappkotlin.utils.repository.profileHost.ProfileHostRepository
import dagger.Module
import dagger.Provides

@Module
class ProfileHostModule {
    @ProfileHostScope
    @Provides
    fun provideRepository(userService: UserService): IProfileHostRepository {
        return ProfileHostRepository(userService)
    }

    @ProfileHostScope
    @Provides
    fun provideInteractor(repository: IProfileHostRepository): ProfileHostInteractor {
        return ProfileHostInteractor(repository)
    }
}
