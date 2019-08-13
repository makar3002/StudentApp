package com.unitbean.studentappkotlin.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.unitbean.studentappkotlin.utils.repository.ApiService
import com.unitbean.studentappkotlin.utils.repository.Preferences
import com.unitbean.studentappkotlin.utils.repository.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
        @Provides
        @Singleton
        fun provideCommonPreferences(context: Context): Preferences {
            return Preferences(context)
        }

        @Provides
        @Singleton
        fun providePreferences(preferences: Preferences): SharedPreferences {
            return preferences.prefs
        }

        @Provides
        @Singleton
        fun providePreferencesEditor(preferences: Preferences): SharedPreferences.Editor {
            return preferences.prefsEditor
        }

        @Provides
        @Singleton
        fun provideUserService(
            prefs: SharedPreferences,
            editor: SharedPreferences.Editor
        ): UserService {
            return UserService(prefs, editor)
        }

        @Provides
        @Singleton
        fun provideApi(): ApiService {
            return ApiService()
        }

}
