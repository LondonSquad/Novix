package com.london.presentation.localization

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
