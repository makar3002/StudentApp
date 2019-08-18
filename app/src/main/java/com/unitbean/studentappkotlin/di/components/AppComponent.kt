package com.unitbean.studentappkotlin.di.components

import android.content.Context
import android.content.SharedPreferences
import com.unitbean.studentappkotlin.di.modules.*

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    val context: Context
    val preferences: SharedPreferences
    val prefsEdit: SharedPreferences.Editor
    fun main(module: MainModule): MainSubcomponent
    fun profileHost(module: ProfileHostModule): ProfileHostSubcomponent
    fun profile(module: ProfileModule): ProfileSubcomponent
    fun auth(module: AuthModule): AuthSubcomponent
    fun lessonsSchedule(module: LessonsScheduleModule): LessonsScheduleSubcomponent

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
