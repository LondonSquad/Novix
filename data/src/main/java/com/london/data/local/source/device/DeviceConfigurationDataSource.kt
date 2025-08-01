package com.london.data.local.source.device

import android.content.Context
import javax.inject.Inject

class DeviceConfigurationDataSource @Inject constructor(
    private val context: Context
) {
    fun getCurrentLanguage(): String {
        return context.resources.configuration.locales[0].language
    }
}