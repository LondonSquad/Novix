package com.london.data.di

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NetworkModule {
    @Single
    fun provideKtorClient(): HttpClient {
        return HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("DEBUGGING", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}
