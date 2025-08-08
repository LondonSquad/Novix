package com.london.data.local.preference

import android.content.Context
import com.london.data.utils.APP_SHARED_PREFS_NAME
import com.london.data.utils.LANGUAGE_KEY
import com.london.domain.language.AppLanguage
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
    return if (systemLanguage.equals("ar", ignoreCase = true)) {
        AppLanguage.ARABIC.code
    } else {
        AppLanguage.ENGLISH.code
    }
}