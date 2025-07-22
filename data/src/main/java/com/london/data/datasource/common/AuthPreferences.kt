package com.london.data.datasource.common

import android.content.SharedPreferences
import androidx.core.content.edit
import com.london.data.BuildConfig
import org.koin.core.annotation.Named

@Named("auth")
class AuthPreferences(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val SESSION_ID = "session_id"
        private const val REQUEST_TOKEN = "request_token"
        private const val USERNAME = "username"
        private const val IS_GUEST = "is_guest"
        private const val GUEST_SESSION_ID = "guest_session_id"
    }

    fun getAuthKey(): String? {
        return BuildConfig.AUTHORIZATION_KEY
    }

    fun saveSessionId(sessionId: String) {
        sharedPreferences.edit { putString(SESSION_ID, sessionId) }
    }

    fun getSessionId(): String? {
        return sharedPreferences.getString(SESSION_ID, null)
    }

    fun saveRequestToken(token: String?) {
        sharedPreferences.edit { putString(REQUEST_TOKEN, token) }
    }

    fun getRequestToken(): String? {
        return sharedPreferences.getString(REQUEST_TOKEN, null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit { putString(USERNAME, username) }
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    fun setGuestMode(isGuest: Boolean) {
        sharedPreferences.edit { putBoolean(IS_GUEST, isGuest) }
    }

    fun isGuestMode(): Boolean {
        return sharedPreferences.getBoolean(IS_GUEST, false)
    }

    fun saveGuestSessionId(guestSessionId: String) {
        sharedPreferences.edit { putString(GUEST_SESSION_ID, guestSessionId) }
    }

    fun getGuestSessionId(): String? {
        return sharedPreferences.getString(GUEST_SESSION_ID, null)
    }

    fun clearAuth() {
        sharedPreferences.edit {
            remove(SESSION_ID)
            remove(REQUEST_TOKEN)
            remove(USERNAME)
            remove(IS_GUEST)
            remove(GUEST_SESSION_ID)
        }
    }

    fun isLoggedIn(): Boolean {
        return getSessionId() != null || getGuestSessionId() != null
    }
}