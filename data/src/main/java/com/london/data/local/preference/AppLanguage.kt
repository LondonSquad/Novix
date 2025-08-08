package com.london.data.local.preference

import android.content.Context
import com.london.data.utils.APP_SHARED_PREFS_NAME
import com.london.data.utils.LANGUAGE_KEY
import com.london.domain.language.AppLanguage

fun readLanguageCode(context: Context): String {
    val prefs = context.getSharedPreferences(
        APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE
    )
    return prefs.getString(
        LANGUAGE_KEY, AppLanguage.ARABIC.code
    ) ?: AppLanguage.ARABIC.code
}