package com.unitbean.studentappkotlin.di

import com.unitbean.studentappkotlin.di.components.*
import com.unitbean.studentappkotlin.di.modules.*

object DIManager {

    lateinit var appComponent: AppComponent
    private var mainSubcomponent: MainSubcomponent? = null
    private var profileSubcomponent: ProfileSubcomponent? = null
    private var profileHostSubcomponent: ProfileHostSubcomponent? = null
    private var authSubcomponent: AuthSubcomponent? = null

    fun getMainSubcomponent(): MainSubcomponent {
        if (mainSubcomponent == null) {
            mainSubcomponent = appComponent.main(MainModule())
        }
        return mainSubcomponent ?: throw IllegalStateException("$mainSubcomponent must not be null")
    }

    fun clearMainSubcomponent() {
        mainSubcomponent = null
    }

    fun getProfileSubcomponent(): ProfileSubcomponent {
        if (profileSubcomponent == null) {
            profileSubcomponent = appComponent.profile(ProfileModule())
        }
        return profileSubcomponent ?: throw IllegalStateException("$profileSubcomponent must not be null")
    }

    fun clearProfileSubcomponent() {
        profileSubcomponent = null
    }

    fun getProfileHostSubcomponent(): ProfileHostSubcomponent {
        if (profileHostSubcomponent == null) {
            profileHostSubcomponent = appComponent.profileHost(ProfileHostModule())
        }
        return profileHostSubcomponent ?: throw IllegalStateException("$profileHostSubcomponent must not be null")
    }

    fun clearProfileHostSubcomponent() {
        profileHostSubcomponent = null
    }

    fun getAuthSubcomponent(): AuthSubcomponent {
        if (authSubcomponent == null) {
            authSubcomponent = appComponent.auth(AuthModule())
        }
        return authSubcomponent ?: throw IllegalStateException("$authSubcomponent must not be null")
    }

    fun clearAuthSubcomponent() {
        authSubcomponent = null
    }

}
