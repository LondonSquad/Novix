package com.london.presentation.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.wrapWithLocale(locale: Locale): Context {
    Locale.setDefault(locale)
    val newConfig = Configuration(resources.configuration)
    newConfig.setLocale(locale)
    newConfig.setLayoutDirection(locale)
    return createConfigurationContext(newConfig)
}
