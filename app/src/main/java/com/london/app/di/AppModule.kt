package com.london.app.di

import android.content.Context
import com.london.data.datasource.device.DeviceConfigurationDataSource
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        DataSourceModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
    ]
)
@ComponentScan("com.london")
class AppModule{
    @Single
    fun provideDeviceConfigurationDataSource(context: Context): DeviceConfigurationDataSource {
        return DeviceConfigurationDataSource(context)
    }
}