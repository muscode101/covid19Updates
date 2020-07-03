package com.muscode.covid19stats.di


import android.app.Application
import com.muscode.covid19stats.di.component.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appComponent)
        }
    }
}