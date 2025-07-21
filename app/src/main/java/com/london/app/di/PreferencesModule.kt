package com.london.app.di

import android.content.Context
import android.content.SharedPreferences
import com.london.data.datasource.preferance.AppPreferencesServiceImpl
import com.london.domain.AppPreferencesService
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

//@ComponentScan("com.london")
//@Module
//class PreferencesModule {
//    @Single
//    fun provideSharedPreferences(context: Context): SharedPreferences {
//        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
//    }
//
//    @Single
//    fun provideAppPreferencesService(sharedPreferences: SharedPreferences): AppPreferencesService {
//        return AppPreferencesServiceImpl(sharedPreferences)
//    }
//}