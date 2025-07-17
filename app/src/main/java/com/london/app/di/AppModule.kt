package com.london.app.di

import android.content.Context
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.di.DataModule
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [DataModule::class])
class AppModule {
    @Single
    fun provideDeviceConfigurationDataSource(context: Context): DeviceConfigurationDataSource {
        return DeviceConfigurationDataSource(context)
    }
}