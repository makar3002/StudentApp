package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.MainModule
import com.unitbean.studentappkotlin.di.scopes.MainScope
import com.unitbean.studentappkotlin.ui.main.presenters.MainPresenter

import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(presenter: MainPresenter)
}
