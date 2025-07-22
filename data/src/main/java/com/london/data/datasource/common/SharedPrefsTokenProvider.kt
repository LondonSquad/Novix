package com.london.data.datasource.common

import com.london.domain.repository.SessionTokenProvider

class SharedPrefsTokenProvider(
    private val authPreferences: AuthPreferences
) : SessionTokenProvider {
    override fun getSessionToken(): String? = authPreferences.getSessionId()
}

