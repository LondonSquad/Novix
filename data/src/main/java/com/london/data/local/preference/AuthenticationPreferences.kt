package com.london.data.local.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.data.BuildConfig
import javax.inject.Inject

class AuthenticationPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun getAuthenticationKey(): String = BuildConfig.AUTHORIZATION_KEY

    fun saveSessionId(id: String) = sharedPreferences.edit { putString(SESSION_ID, id) }

    fun getSessionId(): String? = sharedPreferences.getString(SESSION_ID, null)

    fun saveRequestToken(token: String?) = sharedPreferences.edit { putString(REQUEST_TOKEN, token) }

    fun saveUsername(username: String) = sharedPreferences.edit { putString(USERNAME, username) }

    fun saveAccountId(accountId: Int) = sharedPreferences.edit { putInt(ACCOUNT_ID, accountId) }

    fun getAccountId(): Int = sharedPreferences.getInt(ACCOUNT_ID, 0)

    fun setGuestMode(isGuest: Boolean) = sharedPreferences.edit { putBoolean(IS_GUEST, isGuest) }


    fun isGuestMode(): Boolean = sharedPreferences.getBoolean(IS_GUEST, false)

    fun saveGuestSessionId(id: String) = sharedPreferences.edit { putString(GUEST_SESSION_ID, id) }

    fun getGuestSessionId(): String? = sharedPreferences.getString(GUEST_SESSION_ID, null)

    fun clearAuthentication() {
        sharedPreferences.edit {
            remove(SESSION_ID)
            remove(REQUEST_TOKEN)
            remove(USERNAME)
            remove(ACCOUNT_ID)
            remove(IS_GUEST)
            remove(GUEST_SESSION_ID)
        }
    }

    fun isLoggedIn(): Boolean = getSessionId() != null || getGuestSessionId() != null

    companion object {
        private const val SESSION_ID = "session_id"
        private const val REQUEST_TOKEN = "request_token"
        private const val USERNAME = "username"
        private const val ACCOUNT_ID = "account_id"
        private const val IS_GUEST = "is_guest"
        private const val GUEST_SESSION_ID = "guest_session_id"
    }
}
