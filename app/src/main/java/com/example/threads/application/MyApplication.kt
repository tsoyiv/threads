package com.example.threads.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import retrofitModule
import viewModels

class MyApplication : Application() {

//    lateinit var preferenceManager: PreferenceManager
//        private set

    override fun onCreate() {
        super.onCreate()
//        preferenceManager = PreferenceManager(this)
        startKoin {
            modules(listOf(retrofitModule, viewModels))
            androidContext(this@MyApplication)
        }
    }
}