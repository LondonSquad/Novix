package com.london.data.local.preference

import com.london.domain.repository.SessionTokenProvider
import javax.inject.Singleton

@Singleton
class SharedPrefsTokenProvider(
    private val authPreferences: AuthPreferences
) : SessionTokenProvider {
    override fun getAuthKey(): String? = authPreferences.getAuthKey()
}