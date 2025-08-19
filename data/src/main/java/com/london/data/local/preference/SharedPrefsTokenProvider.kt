package com.london.data.local.preference

import com.london.domain.repository.SessionTokenProvider
import javax.inject.Singleton

@Singleton
class SharedPrefsTokenProvider(
    private val authenticationPreferences: AuthenticationPreferences
) : SessionTokenProvider {
    override fun getAuthenticationKey(): String = authenticationPreferences.getAuthKey()
}