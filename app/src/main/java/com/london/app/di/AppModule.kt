package com.london.app.di

import android.content.Context
import com.london.data.local.source.device.DeviceConfigurationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDeviceConfigurationDataSource(
        @ApplicationContext context: Context
    ): DeviceConfigurationDataSource
        = DeviceConfigurationDataSource(context)

}
