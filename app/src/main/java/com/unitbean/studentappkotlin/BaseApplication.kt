package com.unitbean.studentappkotlin

import android.app.Application

import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.di.components.DaggerAppComponent
import com.vk.api.sdk.VK

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DIManager.appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()
        VK.initialize(applicationContext)
    }

}
