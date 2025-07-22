package com.london.data.datasource.common

import com.london.domain.repository.SessionTokenProvider
import org.koin.core.annotation.Single

@Single
class SharedPrefsTokenProvider(
    private val authPreferences: AuthPreferences
) : SessionTokenProvider {
    override fun getAuthKey(): String? = authPreferences.getRequestToken()
}