package com.omarahmed.dogsample

import android.app.Application
import com.omarahmed.dogsample.di.AppComponent
import com.omarahmed.dogsample.di.AppModule
import com.omarahmed.dogsample.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}