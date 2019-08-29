package com.unitbean.studentappkotlin.di.components

import com.unitbean.studentappkotlin.di.modules.AddLessonModule
import com.unitbean.studentappkotlin.di.modules.AuthModule
import com.unitbean.studentappkotlin.di.scopes.AddLessonScope
import com.unitbean.studentappkotlin.di.scopes.AuthScope
import com.unitbean.studentappkotlin.ui.auth.presenters.AddLessonPresenter
import com.unitbean.studentappkotlin.ui.auth.presenters.AuthPresenter
import dagger.Subcomponent

@AddLessonScope
@Subcomponent(modules = [AddLessonModule::class])
interface AddLessonSubcomponent {
    fun inject(presenter: AddLessonPresenter)
}