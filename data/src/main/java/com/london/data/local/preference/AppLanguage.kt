package com.london.data.local.preference

import android.content.Context
import androidx.core.content.edit
import com.london.data.utils.APP_SHARED_PREFS_NAME
import com.london.data.utils.LANGUAGE_KEY
import com.london.domain.entity.language.AppLanguage
import java.util.Locale

fun readLanguageCode(context: Context): String {
    val prefs = context.getSharedPreferences(
        APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE
    )
    val savedLanguage = prefs.getString(LANGUAGE_KEY, null)

    if (savedLanguage != null) {
        return savedLanguage
    }

    val systemLanguage = Locale.getDefault().language
    val defaultLanguage = if (systemLanguage.equals("ar", ignoreCase = true)) {
        AppLanguage.ARABIC.code
    } else {
        AppLanguage.ENGLISH.code
    }

    prefs.edit { putString(LANGUAGE_KEY, defaultLanguage) }

    return defaultLanguage
}