package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.ProfileHostModule
import com.unitbean.studentappkotlin.di.scopes.ProfileHostScope
import com.unitbean.studentappkotlin.ui.profileHost.presenters.ProfileHostPresenter
import dagger.Subcomponent

@ProfileHostScope
@Subcomponent(modules = [ProfileHostModule::class])
interface ProfileHostSubcomponent {
    fun inject(presenter: ProfileHostPresenter)
}
