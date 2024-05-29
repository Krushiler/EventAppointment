package com.krushiler.eventappointment

import android.app.Application
import com.krushiler.eventappointment.di.dataModule
import com.krushiler.eventappointment.di.viewModelModule
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(viewModelModule, dataModule)
        }
    }
}