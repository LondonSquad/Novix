package com.london.data.local.preference

import com.london.domain.repository.SessionTokenProvider
import org.koin.core.annotation.Single

@Single
class SharedPrefsTokenProvider(
    private val authPreferences: AuthPreferences
) : SessionTokenProvider {
    override fun getAuthKey(): String? = authPreferences.getAuthKey()
}