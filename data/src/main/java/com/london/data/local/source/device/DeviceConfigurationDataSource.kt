package com.london.data.local.source.device

import android.content.Context
import org.koin.core.annotation.Single

@Single
class DeviceConfigurationDataSource(
    private val context: Context
) {
    fun getCurrentLanguage(): String {
        return context.resources.configuration.locales[0].language
    }
}