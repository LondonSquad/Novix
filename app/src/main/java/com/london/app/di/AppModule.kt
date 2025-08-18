package com.london.app.di

import com.london.data.local.source.device.DeviceConfigurationDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun provideDeviceConfigurationDataSource(
        implementation: DeviceConfigurationDataSource
    ): DeviceConfigurationDataSource
}
