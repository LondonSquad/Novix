package com.london.presentation.localization

import com.london.domain.AppPreferencesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalizationManager @Inject constructor(
    private val appPreferencesService: AppPreferencesService
) {
    fun getCurrentLocale(): Locale {
        val languageCode = appPreferencesService.appLanguage.value.code
        return Locale.forLanguageTag(languageCode)
    }

    val localeFlow: Flow<Locale> = appPreferencesService.appLanguage
        .map { Locale.forLanguageTag(it.code) }

    fun setLocale(locale: Locale) {
        appPreferencesService.setAppLanguageCode(locale.toLanguageTag())
    }
}