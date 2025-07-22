package com.london.domain.repository

interface SessionTokenProvider {
    fun getSessionToken(): String?
}
