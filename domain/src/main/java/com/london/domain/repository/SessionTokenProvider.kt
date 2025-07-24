package com.london.domain.repository

interface SessionTokenProvider {
    fun getAuthKey(): String?
}
