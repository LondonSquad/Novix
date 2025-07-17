package com.london.app.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

class NovixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level =Level.DEBUG)
            androidContext(this@NovixApplication)
            modules(AppModule().module)
        }
    }
}