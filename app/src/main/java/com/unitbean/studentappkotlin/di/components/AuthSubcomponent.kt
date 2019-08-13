package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.AuthModule
import com.unitbean.studentappkotlin.di.scopes.AuthScope
import com.unitbean.studentappkotlin.ui.auth.presenters.AuthPresenter
import dagger.Subcomponent

@AuthScope
@Subcomponent(modules = [AuthModule::class])
interface AuthSubcomponent {
    fun inject(presenter: AuthPresenter)
}