package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.ProfileModule
import com.unitbean.studentappkotlin.di.scopes.ProfileScope
import com.unitbean.studentappkotlin.ui.profile.presenters.ProfilePresenter

import dagger.Subcomponent

@ProfileScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileSubcomponent{
    fun inject(presenter: ProfilePresenter)
}
