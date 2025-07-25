package com.london.data.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes =[DataBaseModule::class, NetworkModule::class] )
@ComponentScan("com.london.data.**")
class DataModule {
    @Single
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
}
